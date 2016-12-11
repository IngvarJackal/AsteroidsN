package asteroids.n.utils

import asteroids.n.entities.objects.InputStatus
import asteroids.n.invertY
import asteroids.n.rotate90
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

fun handlePlayerInput(): InputStatus {
    InputStatus.pointVector = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).invertY()
    InputStatus.leftButtonPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    InputStatus.rightButtonPressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
    return InputStatus
}