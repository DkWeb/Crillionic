/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Dirk Weber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.dkweb.crillionic.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import de.dkweb.crillionic.events.DoNothingCollisionHandler;
import de.dkweb.crillionic.events.GameObjectCollisionHandler;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.List;

/**
 * A generic game object, using the component pattern to avoid
 * an inheritance hierarchy.
 */
public class GameObject {
    private String id;
    private Sprite sprite;
    private Body body;
    private GameObjectCollisionHandler collisionHandler;
    private Color color;
    private GameObjectType type;
    private boolean directionLocked;

    public GameObject(String id, Sprite sprite, Body body, Color overwriteColor, GameObjectType type,
                      GameObjectCollisionHandler collisionHandler) {
        this.id = id;
        this.sprite = sprite;
        this.color = overwriteColor;
        this.body = body;
        this.body.setUserData(id);
        this.collisionHandler = collisionHandler;
        this.type = type;
        this.directionLocked = false;
        sprite.setSize(getMaxWidth(), getMaxHeight());
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        if (overwriteColor == null && type.getDefaultColor() != null) {
            sprite.setColor(type.getDefaultColor());
        } else if (overwriteColor != null) {
            sprite.setColor(overwriteColor);
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public String getId() {
        return id;
    }

    /**
     * Must be called after the body has been updated by the world
     * Synchronizes this changes with the sprite, so that they can be rendered
     */
    public void update() {
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(radToDegree(body.getAngle()));
    }

    public float radToDegree(float rad) {
        return (float) ((rad * 180)/ (Math.PI));
    }

    public void move(float forceX, float forceY) {
        if (!directionLocked && (forceX != 0 || forceY != 0)) {
            body.applyForceToCenter(forceX, forceY, true);
        }
    }

    public float getMaxWidth() {
        float maxWidth = 0.0f;
        Shape shape = body.getFixtureList().get(0).getShape();
        if (shape.getType() == Shape.Type.Polygon) {
            PolygonShape polygonShape = (PolygonShape) shape;
            for (int i = 0; i < polygonShape.getVertexCount(); i++) {
                Vector2 oneVertex = new Vector2();
                polygonShape.getVertex(i, oneVertex);
                for (int j = i + 1; j < polygonShape.getVertexCount(); j++) {
                    Vector2 anotherVertex = new Vector2();
                    polygonShape.getVertex(j, anotherVertex);
                    float xDistance = Math.abs(oneVertex.x - anotherVertex.x);
                    if (xDistance > maxWidth) {
                        maxWidth = xDistance;
                    }
                }
            }
        } else if (shape.getType() == Shape.Type.Circle) {
            CircleShape circleShape = (CircleShape) shape;
            maxWidth = circleShape.getRadius() * 2;
        }
        return maxWidth;
    }

    public float getMaxHeight() {
        float maxHeight = 0.0f;
        Shape shape = body.getFixtureList().get(0).getShape();
        if (shape.getType() == Shape.Type.Polygon) {
            PolygonShape polygonShape = (PolygonShape) shape;
            for (int i = 0; i < polygonShape.getVertexCount(); i++) {
                Vector2 oneVertex = new Vector2();
                polygonShape.getVertex(i, oneVertex);
                for (int j = i + 1; j < polygonShape.getVertexCount(); j++) {
                    Vector2 anotherVertex = new Vector2();
                    polygonShape.getVertex(j, anotherVertex);
                    float yDistance = Math.abs(oneVertex.y - anotherVertex.y);
                    if (yDistance > maxHeight) {
                        maxHeight = yDistance;
                    }
                }
            }
        } else if (shape.getType() == Shape.Type.Circle) {
            CircleShape circleShape = (CircleShape) shape;
            maxHeight = circleShape.getRadius() * 2;
        }
        return maxHeight;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean onCollision(List<GameObject> removals, GameObject collidedWith, GameStatistics statistics) {
        if (collisionHandler != null && !(collisionHandler instanceof DoNothingCollisionHandler)) {
            return collisionHandler.onCollision(removals, this, collidedWith, statistics);
        }
        return false;
    }

    public Color getColor() {
        return color;
    }

    public Body getBody() {
        return body;
    }

    public void changeColor(Color color) {
        this.color = color;
        sprite.setColor(color);
    }

    public void setDirectionLocked(boolean locked) {
        directionLocked = locked;
    }

    public GameObjectType getType() {
        return type;
    }
}
