package com.group5.stardrifters.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.group5.stardrifters.utils.B2DBodyBuilder;


public class Circle {
    public Body body;
    public String id;

    public Circle(World world, float X, float Y, float radius, String id) {
        this.body = B2DBodyBuilder.createCircle(world, X, Y, radius);
        this.body.setUserData(this);
    }


}
