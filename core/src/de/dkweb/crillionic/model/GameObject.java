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
 * Created by dirkweber
 */
public class GameObject {
    private String id;
    private Sprite sprite;
    private Body body;
    private GameObjectCollisionHandler collisionHandler;
    private Color color;
    private GameObjectType type;

    public GameObject(String id, Sprite sprite, Body body, Color overwriteColor, GameObjectType type,
                      GameObjectCollisionHandler collisionHandler) {
        this.id = id;
        this.sprite = sprite;
        this.color = overwriteColor;
        this.body = body;
        this.body.setUserData(id);
        this.collisionHandler = collisionHandler;
        this.type = type;
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

    public float degreeToRad(float degree) {
        return (float) ((Math.PI * degree) / 180);
    }

    private void moveBody(Body body, float forceX, float forceY) {
        if (forceX != 0 || forceY != 0) {
            body.applyForceToCenter(forceX, forceY, true);
        }
    }

    public void move(float forceX, float forceY) {
        if (forceX != 0 || forceY != 0) {
            body.applyForceToCenter(forceX, forceY, true);
        }
    }

    public void moveLeft(float forceInNewton) {
        moveBody(body, -1 * forceInNewton, 0f);
    }

    public void moveRight(float forceInNewton) {
        moveBody(body, forceInNewton, 0f);
    }

    public void moveDown(float forceInNewton) {
        moveBody(body, 0f, -1 * forceInNewton);
    }

    public void moveUp(float forceInNewton) {
        moveBody(body, 0f, forceInNewton);
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

    public GameObjectType getType() {
        return type;
    }
}
