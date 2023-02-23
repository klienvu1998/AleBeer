package com.hyvu.alebeer.view.customview

import android.content.Context
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.hyvu.alebeer.R
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.utils.dpToPx

class BeerItemView(context: Context) : LinearLayout(context) {

    interface Listener {
        fun onSave(item: BeerItem)
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        this.mListener = listener
    }

    private lateinit var container: RelativeLayout
    private lateinit var ivBeer: ImageView
    private lateinit var tvBeerName: TextView
    private lateinit var tvBeerPrice: TextView
    private lateinit var btnSave: Button
    private lateinit var edtBeerNote: EditText

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setPadding(dpToPx(context, 10f).toInt(), dpToPx(context, 5f).toInt(), dpToPx(context, 10f).toInt(), dpToPx(context, 5f).toInt())
        }
        orientation = VERTICAL
        initView()
    }

    private fun initView() {
        container = RelativeLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context, 50f).toInt())
        }

        btnSave = Button(context).apply {
            id = R.id.btnSaveBeer
            layoutParams = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(RelativeLayout.CENTER_VERTICAL)
                addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        }

        ivBeer = ImageView(context).apply {
            id = R.id.ivBeer
            layoutParams = RelativeLayout.LayoutParams(dpToPx(context, 100f).toInt(), dpToPx(context, 100f).toInt()).apply {
                addRule(RelativeLayout.CENTER_VERTICAL)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        }

        tvBeerName = TextView(context).apply {
            id = R.id.tvBeerName
            layoutParams = RelativeLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                addRule(RelativeLayout.RIGHT_OF, R.id.ivBeer)
                addRule(RelativeLayout.LEFT_OF, R.id.btnSaveBeer)
                addRule(RelativeLayout.ALIGN_PARENT_TOP)
                setPadding(dpToPx(context, 5f).toInt(), 0, dpToPx(context, 5f).toInt(), 0)
            }
            isSingleLine = true
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

        tvBeerPrice = TextView(context).apply {
            id = R.id.tvBeerPrice
            layoutParams = RelativeLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                addRule(RelativeLayout.RIGHT_OF, R.id.ivBeer)
                addRule(RelativeLayout.LEFT_OF, R.id.btnSaveBeer)
                addRule(RelativeLayout.BELOW, R.id.tvBeerName)
                setPadding(dpToPx(context, 5f).toInt(), 0, dpToPx(context, 5f).toInt(), 0)
            }
            isSingleLine = true
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

        container.addView(btnSave)
        container.addView(ivBeer)
        container.addView(tvBeerName)
        container.addView(tvBeerPrice)

        edtBeerNote = EditText(context).apply {
            id = R.id.edtBeerNote
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            hint = context.getString(R.string.note)
            setBackgroundResource(android.R.color.transparent)
        }

        addView(container)
        addView(edtBeerNote)
    }

    fun bindView(item: BeerItem?) {
        item ?: return
        Glide.with(context).load(item.imageUrl).centerInside().placeholder(R.drawable.image_broken).into(ivBeer)
        tvBeerName.text = item.name
        tvBeerPrice.text = item.price

        if (item.isSaved) {
            btnSave.isGone = true
        } else {
            btnSave.isVisible = true
            btnSave.text = context.getString(R.string.save)
            btnSave.setOnClickListener {
                mListener?.onSave(item)
            }
        }
    }
}