package pt.ulusofona.deisi.cm2223.g21702200

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(context: Context) : SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var listener: OnShakeListener? = null

    // Accelerometer readings
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f

    // Thresholds to detect shake event
    private val shakeThreshold = 10.0f
    private val shakeTimeLapse = 250L


    // Time when the last shake event occurred
    private var lastShakeTime: Long = 0

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        this.listener = listener
    }

    fun start() {
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()

        // Only check for shake events once every shakeTimeLapse milliseconds
        if (currentTime - lastShakeTime > shakeTimeLapse) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val deltaX = x - lastX
            val deltaY = y - lastY
            val deltaZ = z - lastZ

            val acceleration =
                sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble())
            if (acceleration > shakeThreshold) {
                lastShakeTime = currentTime
                listener?.onShake()
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }

    interface OnShakeListener {
        fun onShake()
    }
}
