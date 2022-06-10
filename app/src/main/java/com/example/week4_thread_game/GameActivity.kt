package com.example.week4_thread_game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.MutableLiveData
import com.example.week4_thread_game.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    lateinit var food: ImageView

    private var waitTime = 0L

    private var mapWidth = 0F
    private var mapHeight = 0F

    private var playerWidth = 0F
    private var playerHeight = 0F

    private var foodWidth = 0F
    private var foodHeight = 0F

    val playerPosition = MutableLiveData(PointF())
    val foodPosition = MutableLiveData(PointF(-1f, -1f))
    val createFood = MutableLiveData(false)
    val score1 = MutableLiveData(0)

//    private val randomImage = mutableListOf(
//        R.drawable.salad,
//        R.drawable.fruit,
//        R.drawable.chicken_breast,
//        R.drawable.whey,
//        R.drawable.bad_chinese_food,
//        R.drawable.bad_hamburger,
//        R.drawable.bad_ice_cream,
//        R.drawable.bad_tteok
//
//    )

    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        food = binding.food

        Thread{
            while (true){
                Thread.sleep(500) // 스레드를 진행함에 있어서 몇 ms초만큼 쉬면서 진행해라 / 1000ms = 1s / 1초 마다 다시 이미지 뷰에 띄어준다!
                handler.post{
                    shower()
                    //뱔도의 스레드에서 할수 없는 것을 대신 실행해주는 역할임!!
                }
            }
        }.start()


//        translater()

//
//
//
//        startFallingAnimation()
//
//
        timer()

        totalScore()

//        binding.food.setImageResource(randomImage.random())

//// 가로축으로 한칸 간 animator
//        ObjectAnimator.ofFloat(binding.food, "translationX", 100f).apply {
//            start()
//        }


//        val anim = AnimationUtils.loadAnimation(this, R.anim.down_drop)
//        binding.food.translationX = Math.random().toFloat()
//        binding.food.startAnimation(anim)


//
    }

    private fun shower() {

        val container = food.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var foodW: Float = food.width.toFloat()
        var foodH: Float = food.height.toFloat()


        val newFood = AppCompatImageView(this)
        newFood.setImageResource(R.drawable.icon_food)
        newFood.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newFood)

        foodW *= newFood.scaleX
        foodH *= newFood.scaleY


        newFood.translationX = Math.random().toFloat() * containerW - foodW / 2


        val mover = ObjectAnimator.ofFloat(newFood, View.TRANSLATION_Y, -foodH, containerH + foodH)
        mover.interpolator = AccelerateInterpolator(1f)

        val set = AnimatorSet()
        set.playTogether(mover)
        set.duration = (Math.random() * 1500 + 500).toLong()


        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newFood)
            }
        })

        set.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }


    /**
     *
     *
     */


    private fun totalScore() {

        binding.tvScore.text = "Score : $score"
        intent.putExtra("score", binding.tvScore.text)

    }

    private fun timer() {

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = "Time : ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                val intent = Intent(this@GameActivity, FinishActivity::class.java)
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