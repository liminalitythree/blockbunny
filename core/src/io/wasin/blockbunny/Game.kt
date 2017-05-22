package io.wasin.blockbunny

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.blockbunny.handlers.*

class Game : ApplicationAdapter() {

    lateinit var camViewport: Viewport
        private set
    lateinit var hudViewport: Viewport
        private set
    lateinit var sb: SpriteBatch
        private set
    lateinit var cam: OrthographicCamera
        private set
    lateinit var hudCam: OrthographicCamera
        private set
    lateinit var gsm: GameStateManager
        private set

    private var accum: Float = 0.0f

    companion object {
        const val TITLE = "Block Bunny"
        const val V_WIDTH = 320f
        const val V_HEIGHT = 240f
        const val SCALE = 2
        const val STEP = 1 / 60f

        var res: Content = Content()
    }

    override fun create() {

        Gdx.input.inputProcessor = MyInputProcessor()

        sb = SpriteBatch()

        // set up cam
        cam = OrthographicCamera()
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT)

        // set up hud-cam
        hudCam = OrthographicCamera()
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT)

        gsm = GameStateManager(this)

        camViewport = ExtendViewport(Game.V_WIDTH, Game.V_HEIGHT, cam)
        hudViewport = ExtendViewport(Game.V_WIDTH, Game.V_HEIGHT, hudCam)

        res.loadTexture("images/bunny.png", "bunny")
        res.loadTexture("images/crystal.png", "crystal")
        res.loadTexture("images/hud.png", "hud")
        res.loadTexture("images/bgs.png", "bg")

        // set to begin with Play state
        gsm.pushState(GameStateManager.PLAY)
    }

    override fun render() {
        accum += Gdx.graphics.deltaTime
        while(accum >= STEP) {
            accum -= STEP
            gsm.update(STEP)
            gsm.render()
            MyInput.update()
        }
    }

    override fun dispose() {
    }

    override fun resize(width: Int, height: Int) {
        camViewport.update(width, height)
        hudViewport.update(width, height, true)

        // also propagate resizing event to game state manager
        gsm.updateScreenSize(width, height)
    }
}
