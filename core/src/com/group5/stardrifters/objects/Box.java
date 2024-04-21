package com.group5.stardrifters.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.utils.B2DBodyBuilder;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class Box {
    public Body body;
    public int score;
    public String id;
    public Boolean hit = false;
    public Application app;

    public Box(World world, float X, float Y, float width, float height, String id, Application app) {
        this.body = B2DBodyBuilder.createBox(world, X, Y, width, height);
        this.body.setUserData(this);
        this.score = 100;
        this.id = id;

    }

    public void updateScore() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }

    public int getScore() {
        return this.score;
    }

    public String getId() {
        return this.id;
    }

    public void hit() {
//        System.out.println("Box " + this.id + " has been hit!");
        // Send a message to the server that the box has been hit
//
         this.score--;
//        System.out.println("Score: " + this.score);

//        Send a message to the server that t he box has been hit



        // respawn box in a random location
        this.hit = true;
    }

    public void hitFood(){
//        System.out.println("Box " + this.id + " has eaten food!");

        this.score += 10;
//        System.out.println("Score: " + this.score);

    }

    public void respawn(OrthographicCamera camera) {
        this.hit = false;
        // hide the box for 2 seconds
        this.body.setActive(false);
        body.setActive(true);
        Vector2 location = randomLocation(camera);
//        System.out.println("Respawning boxId:" + id + " at " + location);

        body.setTransform(location, 0);
    }


    private Vector2 randomLocation(OrthographicCamera camera) {
        float x_min = 32 / PPM;
        float x_max = (camera.viewportWidth - 32) / PPM;
        float y_min = 32 / PPM;
        float y_max = (camera.viewportHeight - 32) / PPM;

        float x = (float) (Math.random() * camera.viewportWidth) / PPM;
        float y = (float) (Math.random() * camera.viewportHeight) / PPM;

        while(true) {
            if((x > x_min && x < x_max ) && (y > y_min && y < y_max)) break;
            else {
                x = (float) Math.random() * camera.viewportWidth / PPM;
                y = (float) Math.random() * camera.viewportHeight / PPM;
            }
        }
        return new Vector2(x, y);
    }
}
