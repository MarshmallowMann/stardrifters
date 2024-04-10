//package com.group5.stardrifters.objects;
//
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.World;
//import com.group5.stardrifters.utils.B2DBodyBuilder;
//
//public class Food {
//    public Body body;
//    public String id;
//
//    public Food(World world, float X, float Y, float radius, String id) {
//        this.body = B2DBodyBuilder.createCircle(world, X, Y, radius);
//        this.body.setUserData(this);
//    }
//
//    public void respawn(OrthographicCamera camera) {
//        this.hit = false;
//        // hide the box for 2 seconds
//        this.body.setActive(false);
//        body.setActive(true);
//        Vector2 location = randomLocation(camera);
//        System.out.println("Respawning " + id + " at " + location);
//
//        body.setTransform(location, 0);
//        // Apply random force to the box
//        float x = (float) Math.random() * 500 + 100;
//        float y = (float) Math.random() * 500 + 100;
//        body.applyForceToCenter(new Vector2(x, y), true);
//    }
//}
