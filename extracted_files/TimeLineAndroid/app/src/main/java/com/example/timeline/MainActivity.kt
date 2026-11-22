package com.example.timeline

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private val bg = 0xFF101214.toInt()
    private val fg = 0xFFF4F4F4.toInt()
    private val accent = 0xFFB8E986.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            setBackgroundColor(bg)
        }
        setContentView(root)

        root.addView(TextView(this).apply {
            text = "TIME LINE"
            textSize = 13f
            letterSpacing = .18f
            setTextColor(accent)
        })
        root.addView(TextView(this).apply {
            text = "یک بازه زمانی برای خودت تعریف کن"
            textSize = 22f
            setTextColor(fg)
            setPadding(0, 12, 0, 22)
        })

        val label = EditText(this).apply {
            hint = "عنوان (مثلاً مطالعه)"
            setHintTextColor(0xFF8C9295.toInt())
            setTextColor(fg)
            setSingleLine(true)
            setPadding(12, 10, 12, 10)
            setBackgroundColor(0xFF24282B.toInt())
        }
        root.addView(label, matchWrap())

        val hours = numberInput("ساعت", "48")
        val minutes = numberInput("دقیقه", "0")
        val durationRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        durationRow.addView(hours, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        durationRow.addView(Space(this), LinearLayout.LayoutParams(12, 1))
        durationRow.addView(minutes, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        durationRow.setPadding(0, 14, 0, 14)
        root.addView(durationRow)

        val mode = RadioGroup(this).apply { orientation = RadioGroup.HORIZONTAL }
        val down = RadioButton(this).apply { text = "شمارش معکوس"; setTextColor(fg); id = 1 }
        val up = RadioButton(this).apply { text = "شمارش رو به جلو"; setTextColor(fg); id = 2 }
        mode.addView(down); mode.addView(up)
        mode.check(if (TimerStore.countDown(this)) 1 else 2)
        root.addView(mode)

        val orientation = RadioGroup(this).apply { orientation = RadioGroup.HORIZONTAL; setPadding(0, 8, 0, 8) }
        val horizontal = RadioButton(this).apply { text = "افقی"; setTextColor(fg); id = 3 }
        val vertical = RadioButton(this).apply { text = "عمودی"; setTextColor(fg); id = 4 }
        orientation.addView(horizontal); orientation.addView(vertical)
        orientation.check(if (TimerStore.vertical(this)) 4 else 3)
        root.addView(orientation)

        val save = Button(this).apply {
            text = "شروع / تنظیم مجدد تایمر"
            setTextColor(bg)
            setBackgroundColor(accent)
            setOnClickListener {
                val h = hours.text.toString().toLongOrNull() ?: 0L
                val m = minutes.text.toString().toLongOrNull() ?: 0L
                val duration = (h * 60L + m) * 60_000L
                if (duration <= 0L) {
                    Toast.makeText(this@MainActivity, "مدت زمان باید بیشتر از صفر باشد", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                TimerStore.save(this@MainActivity, duration, mode.checkedRadioButtonId == 1, orientation.checkedRadioButtonId == 4, label.text.toString())
                TimelineWidgetProvider.schedule(this@MainActivity)
                TimelineWidgetProvider.refreshAll(this@MainActivity)
                Toast.makeText(this@MainActivity, "تایمر ذخیره شد؛ ویجت را به صفحه اضافه کن", Toast.LENGTH_LONG).show()
            }
        }
        root.addView(save, matchWrap())

        root.addView(TextView(this).apply {
            text = "برای صفحه قفل سامسونگ: پس از نصب، ویجت TimeLine را به صفحه اصلی اضافه کن؛ سپس در Good Lock > LockStar بررسی کن که ویجت در فهرست ویجت‌های قابل افزودن باشد."
            textSize = 13f
            setTextColor(0xFFADB3B6.toInt())
            setPadding(0, 24, 0, 0)
        })
    }

    private fun numberInput(hintText: String, value: String) = EditText(this).apply {
        hint = hintText
        setHintTextColor(0xFF8C9295.toInt())
        setTextColor(fg)
        setSingleLine(true)
        inputType = android.text.InputType.TYPE_CLASS_NUMBER
        setText(value)
        setPadding(12, 10, 12, 10)
        setBackgroundColor(0xFF24282B.toInt())
    }

    private fun matchWrap() = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}
