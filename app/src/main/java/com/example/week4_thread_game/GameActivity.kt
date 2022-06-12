package com.example.week4_thread_game

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.week4_thread_game.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var score = 0

    private var runnableFood: Runnable = Runnable { }

    private var screenWidth: Int = 0

    private var screenHeight: Int = 0

    private val handler = Handler()

    private var timer: Int = 0

    private var waitTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = 0


        Thread {
            while (true) {
                Thread.sleep(500) // 스레드를 진행함에 있어서 몇 ms초만큼 쉬면서 진행해라 / 1000ms = 1s / 1초 마다 다시 이미지 뷰에 띄어준다!
                handler.post {
                    //뱔도의 스레드에서 할수 없는 것을 대신 실행해주는 역할임!!
                }
            }
        }.start()

        game()

        getScreenDisplay()

        binding.hamburger.setOnClickListener {
            eatEffect()
            score++
            binding.tvScore.text = "Score : $score"
        }

        timer()

    }

    private fun game() {

        timer = 30

        runnableFood = object : Runnable {
            override fun run() {
                imageViewRandom()
                handler.postDelayed(this, 1200)
                if (timer == 0) {
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.post(runnableFood)
    }

    private fun imageViewRandom() {
        var imageWidth = (0..screenWidth).random()
        var imageHeight = (0..screenHeight).random()
        binding.hamburger.translationX = (imageWidth).toFloat()
        binding.hamburger.translationY = (imageHeight).toFloat()
        binding.hamburger.setImageResource(R.drawable.icon_food)
    }

    private fun getScreenDisplay() {
        var layoutConst = findViewById<ConstraintLayout>(R.id.layout_const)
        layoutConst.viewTreeObserver.addOnGlobalLayoutListener {
            screenWidth = layoutConst.width
            screenHeight = layoutConst.height
        }
    }

    private fun eatEffect() {
        binding.hamburger.setImageResource(R.drawable.icon_pig)
    }

    private fun timer() {

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = "Time : ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                val intent = Intent(this@GameActivity, FinishActivity::class.java)
                intent.putExtra("Score", score.toString())
                startActivity(intent)
                finish()
            }

        }.start()

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish() // 액티비티 종료
        }
    }

}
