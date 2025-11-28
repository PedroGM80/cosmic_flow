package dev.pgm.cosmic_flow

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for [CosmicFlowScreen].
 *
 * Tests basic rendering and UI interactions.
 */
@RunWith(AndroidJUnit4::class)
class CosmicFlowScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cosmicFlowScreen_displaysTitle() {
        composeTestRule.setContent {
            CosmicFlowScreen()
        }

        // Verify the title is displayed
        composeTestRule.onNodeWithText("✨ Cosmic Flow ✨", substring = true).assertExists()
    }

    @Test
    fun cosmicFlowScreen_displaysSubtitle() {
        composeTestRule.setContent {
            CosmicFlowScreen()
        }

        // Verify the subtitle is displayed
        composeTestRule.onNodeWithText("Shaders", substring = true).assertExists()
    }

    @Test
    fun cosmicFlowScreen_displaysControlsOverlay() {
        composeTestRule.setContent {
            CosmicFlowScreen(particleCount = 500)
        }

        // Verify the controls overlay shows the particle count
        composeTestRule.onNodeWithText("Particles: 500", substring = true).assertExists()
    }

    @Test
    fun cosmicFlowScreen_rendersWithCustomParticleCount() {
        composeTestRule.setContent {
            CosmicFlowScreen(particleCount = 100)
        }

        // Should render without crashing
        composeTestRule.onNodeWithText("Particles: 100", substring = true).assertExists()
    }

    @Test
    fun cosmicFlowScreen_rendersWithDefaultParticleCount() {
        composeTestRule.setContent {
            CosmicFlowScreen()
        }

        // Should render with default 720 particles
        composeTestRule.onNodeWithText("Particles: 720", substring = true).assertExists()
    }
}
