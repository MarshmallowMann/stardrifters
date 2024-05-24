package com.group5.stardrifters.screens;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.group5.stardrifters.Application;
import com.group5.stardrifters.managers.GameScreenManager;
import com.group5.stardrifters.objects.Box;
import com.group5.stardrifters.objects.Circle;
import com.group5.stardrifters.objects.Food;
import com.group5.stardrifters.utils.*;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.group5.stardrifters.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen {
    private HUD hud;
    SpriteBatch batch = new SpriteBatch();
    Texture bg = new Texture("galaxy_bg.png");
    BitmapFont font = new BitmapFont();
    OrthographicCamera camera;

    public ArrayList<String> getChatHistory() {
        return chatHistory;
    }
    public ArrayList<String> moveHistory() { return  moveHistory;}

    ArrayList<String> chatHistory = ClientProgram.chatHistory;
    ArrayList<String> moveHistory = ClientProgram.moveHistory;
    // Box2D
    World world;
    Box2DDebugRenderer b2dr;
    // Box2D body
    // Array of boxes
    ArrayList<Box> boxes = new ArrayList<Box>();
    ArrayList<Food> foods = new ArrayList<Food>();
    Circle circle;
    Integer offsetY = 200;
    // Gravitational constant
    float G = 30f;
    boolean shouldMoveDynamicBody = false;
    // RayHandler
     RayHandler rayHandler;

     ArrayList<GameObject> bodies = new ArrayList<GameObject>();
     ArrayList<GameObject> foodBodies = new ArrayList<GameObject>();
    boolean allAreFoods = true;


    public GameScreen(final Application app) {
        super(app);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
        b2dr = new Box2DDebugRenderer();
        hud = new HUD(batch);
    }

    @Override
    public void show() {
         // Player count
        int playerCount = ClientProgram.playerCount;

        MyTextInputListener Textlistener = new MyTextInputListener();
        Gdx.input.getTextInput(Textlistener, "Dialog Title", "Initial Textfield Value", "Hint Value");
        world = new World(new Vector2(0, 0f), false);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f);
        rayHandler.setBlurNum(3);
        createWalls();
        DirectionalLight light1 = new DirectionalLight(rayHandler, 100, Color.BLUE, 45);
        DirectionalLight light2 = new DirectionalLight(rayHandler, 100, Color.BLUE, 135);
        DirectionalLight light3 = new DirectionalLight(rayHandler, 100, Color.BLUE, 225);
        DirectionalLight light4 = new DirectionalLight(rayHandler, 100, Color.BLUE, 315);

         // Array of colors
         Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE};

        circle = new Circle(world, camera.viewportWidth/2, camera.viewportHeight/2, 16f, "circle");
        for (int i = 0; i < 2; i++) {
            Box box = new Box(world, camera.viewportWidth/2, camera.viewportHeight/2, 32, 32, "Player" + (i+1), app);
            box.respawn(camera);
            Vector2 linearForce = new Vector2(0, 1000);
            box.body.applyForceToCenter(linearForce, true);
            box.body.setAngularVelocity(0.2f);
            // Create a PointLight for each Box and attach it to the Box's body
            PointLight pointLight = new PointLight(rayHandler, 50);

            // reduce light intensity
            pointLight.setDistance(2.5f);
            pointLight.attachToBody(box.body);

             // Set the color of the PointLight to a color from the array
            pointLight.setColor(colors[i % colors.length]);
            boxes.add(box);
        }

        for (int i = 0; i < 10; i++) {
            Food food = new Food(world, camera.viewportWidth/2, camera.viewportHeight/2, 16, 16, "Food" + (i+1),app);
            food.respawn(camera);
            foods.add(food);

            // Create a PointLight for each Food and attach it to the Food's body
            PointLight pointLight = new PointLight(rayHandler, 50); // Smaller number for less light

            // reduce light intensity
            pointLight.setDistance(1.5f);
//            Make it more intense
            pointLight.setSoftnessLength(0f);
            pointLight.attachToBody(food.body);

            // Set the color of the PointLight to blue
            pointLight.setColor(Color.ORANGE);

        }


        PointLight pointLight = new PointLight(rayHandler, 100);
        pointLight.attachToBody(circle.body);

        // Set the color of the PointLight to black
        pointLight.setColor(Color.BLACK);
        pointLight.setDistance(16f);
        pointLight.setXray(true);

        MyContactListener contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);

        if (Objects.equals(Application.playerName, "Player1")) {
            //        store all box bodies

            for (Box box : boxes) {
                bodies.add(new GameObject(box.body.getPosition(), box.body.getLinearVelocity(), box.body.getAngle(), box.body.getAngularVelocity(), box.id));
            }

            // sync the bodies with the server
            //print the id of the bodies
            for (GameObject body : bodies) {
                System.out.println(body.getObjectName());
            }
            ClientProgram.syncBodies(bodies);
            ClientProgram.syncFood(foodBodies);
        }

    }

    @Override
    public void update(float delta) throws IOException {
//        Get chat history from client program
       world.step(1f /Application.APP_FPS, 6, 2);
        Vector2 circlePosition = circle.body.getPosition();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // if the chatHistory array is more than 5, remove the first element

            TextField textField = hud.userInputField;
            String message = textField.getText();
            textField.setText("");
            //aSystem.out.println("Sending message: " + message);
            try {
                ClientProgram.sendMessageToServer(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Sync the game state 10 frames per second
        if (Objects.equals(Application.playerName, "Player1") &&  (Gdx.graphics.getFrameId() % 10 == 0)) {
            //store all box bodies
            for (Box box : boxes) {
                bodies.add(new GameObject(box.body.getPosition(), box.body.getLinearVelocity(), box.body.getAngle(), box.body.getAngularVelocity(), box.id));
            }
            // sync the bodies with the server
            ClientProgram.syncBodies(bodies);
            bodies.clear();
        }

        if (Objects.equals(Application.playerName, "Player1")) {
            //store all box bodies
            for (Food food : foods) {
                if(food.hit){
                    food.respawn(camera);
                    foodBodies.add(new GameObject(food.body.getPosition(), new Vector2(0,0), 0, 0, food.id));
                    ClientProgram.syncFood(foodBodies);

                }
            }
            foodBodies.clear();
            // sync the bodies with the server

        }


        for (Box box : boxes) {
            Vector2 boxPosition = box.body.getPosition();
            Vector2 direction = circlePosition.cpy().sub(boxPosition);
            float distance = direction.len();
            if (distance > 0) direction.nor();
            if (box.hit) {
                //app.clientProgram.sendMessage( box.id + "has eaten food!. Score" + box.score);
                if (Application.playerName.equals("Player1")) {
                    box.respawn(camera);
                }

            }
            float forceMagnitude = (G * 15f* box.body.getMass()) / (distance * distance);
            // Apply the gravitational force to the rectangle body
            Vector2 force = direction.scl(forceMagnitude);

            box.body.applyForceToCenter(force, true);
            // Apply y axis linear force so that it orbits the circle
        }


    //    On space bar click, apply impulse to the box away from the circle
        // if moveHistory is not empty, apply impulse to the box away from the circle
        if (!moveHistory.isEmpty()) {
            //print move history
            System.out.println(moveHistory);
            for (Box box : boxes) {
                if (box.id.equals(moveHistory.get(0))) {
                    Vector2 boxPosition = box.body.getPosition();
                    Vector2 impulseDirection = boxPosition.cpy().sub(circlePosition);
                    impulseDirection.nor();
                    float impulseMagnitude = 25f;
                    box.body.applyLinearImpulse(impulseDirection.scl(impulseMagnitude), boxPosition, true);
                    moveHistory.remove(0);
                    break;
                }
            }
        }
if (!ClientProgram.gameObjects.isEmpty()) {
    // get the box object from the game object
    GameObject gameObject = ClientProgram.gameObjects.get(0);
    for (Box box : boxes) {
        if (box.id.equals(gameObject.getObjectName())) {
            box.body.setTransform(gameObject.getPosition(), gameObject.getRotation());
            box.body.setLinearVelocity(gameObject.getVelocity());
            box.body.setAngularVelocity(gameObject.getAngularVelocity());
            break;
        }
    }
    ClientProgram.gameObjects.remove(0);
} else {
for (Box box : boxes) {
    box.prevPosition = box.body.getPosition().cpy();
    box.prevVelocity = box.body.getLinearVelocity().cpy();
}

//        If syncGamePackets is not empty, sync the game state
//        if (!ClientProgram.syncGamePackets.isEmpty()) {
//            System.out.println("Syncing game state");
//            ArrayList<GameObject> bodies = ClientProgram.syncGamePackets.get(0).getBodies();
//            for (GameObject body : bodies) {
//                for (Box box : boxes) {
//                    if (box.id.equals(body.getObjectName())) {
//                        box.body.setTransform(body.getPosition(), body.getRotation());
//                        box.body.setLinearVelocity(body.getVelocity());
//                        box.body.setAngularVelocity(body.getAngularVelocity());
//                        break;
//                    }
//                }
//            }
//            ClientProgram.syncGamePackets.remove(0);
//        }
        // Step 2: When you receive a new position and velocity from the server, set them as the target position and velocity
if (!ClientProgram.syncGamePackets.isEmpty()) {
    //System.out.println("Syncing game state");
    ArrayList<GameObject> bodies = ClientProgram.syncGamePackets.get(0).getBodies();
    for (GameObject body : bodies) {
        for (Box box : boxes) {
            if (box.id.equals(body.getObjectName())) {
                box.targetPosition = body.getPosition();
                box.targetVelocity = body.getVelocity();
                box.body.setAngularVelocity(body.getAngularVelocity());
                break;
            }
        }
        for (Food food : foods) {
            if (food.id.equals(body.getObjectName())) {
                //System.out.println("Syncing food");
                food.body.setTransform(body.getPosition(), body.getRotation());
                break;
            }
        }
    }
    ClientProgram.syncGamePackets.remove(0);
}

// Step 3: In each frame, gradually move the Box object from its current position and velocity towards the target position and velocity
for (Box box : boxes) {
    if (box.targetPosition != null && box.targetVelocity != null) {
        Vector2 newPosition = box.prevPosition.lerp(box.targetPosition, delta);
        Vector2 newVelocity = box.prevVelocity.lerp(box.targetVelocity, delta);
        box.body.setTransform(newPosition, box.body.getAngle());
        box.body.setLinearVelocity(newVelocity);
    }
}
}

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ClientProgram.sendControlMessageToServer("Impulse");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            // Calculate impulse direction away from the circle
            Vector2 boxPosition = boxes.get(1).body.getPosition();
            Vector2 impulseDirection = boxPosition.cpy().sub(circlePosition);
            impulseDirection.nor();
            // Calculate impulse magnitude
            float impulseMagnitude = 15f;
            // Apply impulse to the box
            boxes.get(1).body.applyLinearImpulse(impulseDirection.scl(impulseMagnitude), boxPosition, true);
        }
        //generate impulse based on server message

        stage.act(delta);
        hud.update(delta);
        this.camera.update();
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        // Continue with your existing rendering code
        batch.begin();
        batch.draw(bg, 0, 0);

        batch.end();
        b2dr.render(world, camera.combined.cpy().scl(PPM));
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));
        rayHandler.updateAndRender();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        batch.begin();

        for(int i = 1; i < chatHistory.size()+1; i++) {
            font.draw(batch, chatHistory.get(i-1), 10, i*(-20) + offsetY );
        }
        font.draw(batch, Application.playerName + ": ", 20, 20);
        batch.end();
        stage.draw();
        hud.stage.draw();
        Gdx.input.setInputProcessor(hud.stage);
        if(gameOver()) {
            app.gsm.setScreen(GameScreenManager.STATE.GAMEOVER);
        }
    }

    public boolean gameOver() {
        return hud.isTimeUp();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        b2dr.dispose();
        rayHandler.dispose();  // Dispose the RayHandler
    }

    private void createWalls() {
        Vector2[] verts = new Vector2[5];
        verts[0] = new Vector2(1f / PPM, 0);
        verts[1] = new Vector2(camera.viewportWidth / PPM, 0);
        verts[2] = new Vector2(camera.viewportWidth / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[3] = new Vector2(1f / PPM, (camera.viewportHeight - 1f) / PPM);
        verts[4] = new Vector2(1f / PPM, 0);
        B2DBodyBuilder.createChainShape(world, verts, true);
    }

    public void setShouldMoveDynamicBody(boolean shouldMoveDynamicBody) {
        this.shouldMoveDynamicBody = shouldMoveDynamicBody;
    }


}
