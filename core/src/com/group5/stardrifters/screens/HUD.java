package com.group5.stardrifters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.group5.stardrifters.Application;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.group5.stardrifters.utils.ClientProgram;
import com.group5.stardrifters.utils.MyTextInputListener;

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private static Integer score;
    private float timeCount;
    private boolean timeUp;
    TextField userInputField;
    Label countdownLabel;
    static Label scoreLabel;
    Label chatHistory;

    public HUD(SpriteBatch batch) {
        worldTimer = ClientProgram.timeLeft;
    timeCount = 0;
    score = 0;
    viewport = new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, new OrthographicCamera());
    this.stage = new Stage(viewport, batch);

    Table table = new Table();
    table.top();
    table.setFillParent(true);

    countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    scoreLabel = new Label(String.format("%02d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

    table.add(countdownLabel).expandX().padTop(10);
    table.add(scoreLabel).expandX().padTop(10);

    TextField.TextFieldStyle style = new TextField.TextFieldStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.WHITE;
    TextField usernameTextField = new TextField("", style);
    usernameTextField.setPosition(80,8);
    usernameTextField.setSize(200, 14);
    usernameTextField.setMessageText("Enter your messages");

    stage.addActor(usernameTextField); // Add the TextField to the stage

   // Gdx.input.setInputProcessor(stage); // Set the input processor to the stage

    table.row();  // Add a new row to the table
    stage.addActor(table);
    this.userInputField = usernameTextField;


    }

    public void update(float delta) {
        timeCount += delta;
        if(timeCount >= 1) {
            if(worldTimer > 0) {
                worldTimer--;
            }
            else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        score = ClientProgram.score;
        scoreLabel.setText(String.format("%03d", score));
    }

    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }
}
