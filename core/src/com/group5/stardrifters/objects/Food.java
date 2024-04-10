package com.group5.stardrifters.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.group5.stardrifters.utils.B2DBodyBuilder;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class Food {
    public Body body;
    public String id;
    public Boolean hit = false;

    public Food(World world, float X, float Y, float width, float height) {
        this.body = B2DBodyBuilder.createTriangle(world, X, Y, width, height);
        this.body.setUserData(this);
    }

    public void hit() {
        System.out.println("Food " + this.id + " has been hit!");
        // respawn food in a random location
        this.hit = true;
    }

    public void respawn(OrthographicCamera camera) {
        this.hit = false;
        // hide the box for 2 seconds
        this.body.setActive(false);
        body.setActive(true);
        Vector2 location = randomLocation(camera);
        System.out.println("Respawning " + id + " at " + location);

        body.setTransform(location, 0);
    }

    private Vector2 randomLocation(OrthographicCamera camera) {
        float x_min = (camera.viewportWidth/2 - 32) / PPM;
        float x_max = (camera.viewportWidth/2 + 32) / PPM;
        float y_min = (camera.viewportHeight/2 - 32) / PPM;
        float y_max = (camera.viewportHeight/2 + 32) / PPM;

        float x = (float) (Math.random() * camera.viewportWidth) / PPM;
        float y = (float) (Math.random() * camera.viewportHeight) / PPM;
        while ( (x > x_min && x < x_max ) || (y > y_min && y < y_max || (x < 1) || (y < 1) || (x > camera.viewportWidth/PPM) || y > camera.viewportHeight/PPM) ) {

            x = (float) Math.random() * camera.viewportWidth / PPM;
            y = (float) Math.random() * camera.viewportHeight / PPM;
        }

        return new Vector2(x, y);
    }
}
