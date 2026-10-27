package org.firstinspires.ftc.teamcode.teleop.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.lights.Color;
import org.firstinspires.ftc.teamcode.subsystems.lights.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.subsystems.lights.GoBildaPrismDriver.LayerHeight;
import org.firstinspires.ftc.teamcode.subsystems.lights.PrismAnimations;

@Config
@TeleOp(name = "Lights Test", group = "testing")
public class LightsTest extends OpMode {
    GoBildaPrismDriver prism;

    PrismAnimations.Solid base = new PrismAnimations.Solid(Color.BLUE);
    PrismAnimations.RainbowSnakes animation = new PrismAnimations.RainbowSnakes();

    public static int baseBrightness = 50;
    public static int startIndex = 0;
    public static int stopIndex = 12;

    public static int numberSnakes = 2;
    public static int snakeLength = 3;
    public static int snakeSpacing = 6;
    public static float speed = 0.5f;
    public static boolean UPDATE = false;


    @Override
    public void init() {
        prism = hardwareMap.get(GoBildaPrismDriver.class,"prism");

        base.setBrightness(baseBrightness);
        base.setStartIndex(startIndex);
        base.setStopIndex(stopIndex);

        animation.setNumberOfSnakes(numberSnakes);
        animation.setSnakeLength(snakeLength);
        animation.setSpacingBetween(snakeSpacing);
        animation.setSpeed(speed);
    }

    @Override
    public void loop() {

        if (UPDATE) {
            UPDATE = false;
            /*
             * Here we insert and update the animation to the Prism, this by default does not
             * save it to an Artboard, it just starts the Animation playing. If you have
             * already inserted an animation at a layer height, you can instead call
             * .updateAnimationFromIndex(LayerHeight.LAYER_0) to update an animation at a
             * specific layer height without overwriting it completely.
             */
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, base);
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, animation);
        }

        prism.updateAnimationFromIndex(LayerHeight.LAYER_0);
        prism.updateAnimationFromIndex(LayerHeight.LAYER_1);
    }
}
