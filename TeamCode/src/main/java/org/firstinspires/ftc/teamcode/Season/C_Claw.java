package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class C_Claw extends LinearOpMode {

    boolean isOpen = false;

    @Override
    public void runOpMode() throws InterruptedException {

        //Servo Declaration
        Servo ServoL = hardwareMap.servo.get("Left");
        Servo ServoR = hardwareMap.servo.get("Right");

        //Initialise Servos
        ServoL.setPosition(0.3);
        ServoR.setPosition(0.3);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Hold to Grab
            if(gamepad1.x) {
                ServoL.setPosition(0.55);
                ServoR.setPosition(0.05);
            }
            else {
                ServoL.setPosition(0.3);
                ServoR.setPosition(0.3);
            }

            //Toggle Grab
            if (gamepad1.a && !isOpen) {
                ServoL.setPosition(0.55);
                ServoR.setPosition(0.05);
                isOpen = false;
            }
            if (gamepad1.b && isOpen) {
                ServoL.setPosition(0.3);
                ServoR.setPosition(0.3);
                isOpen = true;
            }

            //Telemetry Update
            telemetry.addData("Claw State", isOpen ? "Open" : "Closed");
            telemetry.update();
        }
    }
}
