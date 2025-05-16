package org.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
    }

    public void move() {
        // Movimiento con el teclado
        float moveSpeed = 1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.x += Math.sin(Math.toRadians(yaw)) * moveSpeed;
            position.z -= Math.cos(Math.toRadians(yaw)) * moveSpeed;
            position.y -= Math.sin(Math.toRadians(pitch)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x -= Math.sin(Math.toRadians(yaw)) * moveSpeed;
            position.z += Math.cos(Math.toRadians(yaw)) * moveSpeed;
            position.y += Math.sin(Math.toRadians(pitch)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x += Math.sin(Math.toRadians(yaw - 90)) * moveSpeed;
            position.z -= Math.cos(Math.toRadians(yaw - 90)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x -= Math.sin(Math.toRadians(yaw - 90)) * moveSpeed;
            position.z += Math.cos(Math.toRadians(yaw - 90)) * moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= moveSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        	Display.destroy();
        	System.exit(0);
        }
        
        // Movimiento con el ratón
        float mouseDX = Mouse.getDX() * 0.1f;
        float mouseDY = Mouse.getDY() * 0.1f;
        yaw += mouseDX;
        pitch -= mouseDY;

        // Limitar el ángulo de pitch para evitar que la cámara gire completamente
        pitch = Math.max(-90, Math.min(90, pitch));
        Mouse.setGrabbed(true);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
