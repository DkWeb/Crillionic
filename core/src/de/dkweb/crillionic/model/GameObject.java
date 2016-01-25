package de.dkweb.crillionic.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by dirkweber
 */
public class GameObject {
    private String id;
    private Sprite sprite;
    private Body body;

    public GameObject(String id, Sprite sprite, Body body, Color color) {
        this.id = id;
        this.sprite = sprite;
        this.body = body;
        this.body.setUserData(id);
        sprite.setSize(getMaxWidth(), getMaxHeight());
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setOriginCenter();
        if (color != null) {
            sprite.setColor(color);
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

    public float degreeToRad(float degree) {
        return (float) ((Math.PI * degree) / 180);
    }

    public void moveLeft(float forceInNewton) {
        body.applyForceToCenter(-1 * forceInNewton, 0f, true);
    }

    public void moveRight(float forceInNewton) {
        body.applyForceToCenter(forceInNewton, 0f, true);
    }

    public void moveDown(float forceInNewton) {
        body.applyForceToCenter(0f, -1 * forceInNewton, true);
    }

    public void moveUp(float forceInNewton) {
        body.applyForceToCenter(0f, forceInNewton, true);
    }

    public void rotate(float degree) {
        body.setTransform(body.getPosition(), degreeToRad(degree));
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
}
