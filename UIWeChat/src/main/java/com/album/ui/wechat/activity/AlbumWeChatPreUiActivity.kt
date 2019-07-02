package com.album.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.album.Album
import com.album.AlbumBundle
import com.album.AlbumConst
import com.album.core.*
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseActivity
import com.album.callback.AlbumPreCallback
import com.album.ui.fragment.PrevFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.album_wechat_preview_activity.*


/**
 * @author y
 * @create 2018/12/3
 */
class AlbumWeChatPreUiActivity : AlbumBaseActivity(), AlbumPreCallback {

    companion object {
        private const val TYPE_HAS_ORIGINAL_IMAGE = "original_image"
        fun start(albumBundle: AlbumBundle, uiBundle: AlbumWeChatUiBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, originalImage: Boolean, fragment: Fragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(AlbumConst.TYPE_PRE_SELECT, multiplePreviewList)
                putInt(AlbumConst.TYPE_PRE_POSITION, position)
                putBoolean(TYPE_HAS_ORIGINAL_IMAGE, originalImage)
                putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, AlbumWeChatPreUiActivity::class.java).putExtras(bundle), AlbumConst.TYPE_PRE_REQUEST_CODE)
        }
    }

    override val layoutId: Int = com.album.ui.wechat.R.layout.album_wechat_preview_activity

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumWeChatUiBundle
    private lateinit var prevFragment: PrevFragment

    override fun initView() {
        album_wechat_preview_ui_original_image.isChecked = intent.extras?.getBoolean(TYPE_HAS_ORIGINAL_IMAGE, false)
                ?: false
        album_wechat_preview_ui_title_send.setOnClickListener {
            if (prevFragment.getSelectEntity().isEmpty()) {
                Album.instance.albumListener?.onAlbumContainerPreSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumResources(prevFragment.getSelectEntity())
            prevFragment.isRefreshAlbumUI(isRefresh = false, isFinish = albumUiBundle.previewSelectOkFinish)
        }
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumWeChatUiBundle()

        window.statusBarColor(ContextCompat.getColor(this, albumUiBundle.statusBarColor))
        album_wechat_preview_ui_toolbar.setTitle(albumUiBundle.toolbarText)
        album_wechat_preview_ui_toolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        album_wechat_preview_ui_toolbar.navigationIcon = drawable
        album_wechat_preview_ui_toolbar.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.toolbarBackground))
        album_wechat_preview_ui_toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(albumUiBundle.previewFinishRefresh, false) }
        if (hasL()) {
            album_wechat_preview_ui_toolbar.elevation = albumUiBundle.toolbarElevation
        }

        album_wechat_preview_ui_bottom_prev.setHasFixedSize(true)
        album_wechat_preview_ui_bottom_prev.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        album_wechat_preview_ui_bottom_prev.adapter = object : RecyclerView.Adapter<PrevViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevViewHolder {
                return PrevViewHolder(LayoutInflater.from(parent.context).inflate(com.album.ui.wechat.R.layout.album_wechat_pre_bottom_pre_item, parent, false))
            }

            override fun getItemCount(): Int = prevFragment.getSelectEntity().size

            override fun onBindViewHolder(holder: PrevViewHolder, position: Int) {
                val arrayList = prevFragment.getSelectEntity()
                val albumEntity = arrayList[position]
                val currentEntity = prevFragment.getCurrentItem()
                Glide.with(holder.itemView.context).load(albumEntity.path).into(holder.imageView)
                holder.rootView.setBackgroundColor(if (currentEntity.path == albumEntity.path) Color.WHITE else ContextCompat.getColor(this@AlbumWeChatPreUiActivity, com.album.ui.wechat.R.color.album_wechat_ui_bottom_root))
                holder.itemView.setOnClickListener { prevFragment.setCurrentItem(prevFragment.getAllList().indexOf(albumEntity)) }
            }

        }
        initFragment()
    }

    class PrevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(com.album.ui.wechat.R.id.album_wechat_pre_bottom_pre_item_iv)
        val rootView: FrameLayout = itemView.findViewById(com.album.ui.wechat.R.id.album_wechat_pre_bottom_pre_item_root)
    }

    override fun onChangedCheckBoxCount(selectCount: Int) {
        album_wechat_preview_ui_title_send.isEnabled = selectCount != 0
        album_wechat_preview_ui_bottom_prev.adapter?.notifyDataSetChanged()
        if (selectCount == 0) {
            album_wechat_preview_ui_bottom_prev.hide()
            album_wechat_preview_ui_title_send.text = getString(com.album.ui.wechat.R.string.album_wechat_ui_title_send)
        } else {
            album_wechat_preview_ui_bottom_prev.show()
            album_wechat_preview_ui_title_send.text = String.format(getString(com.album.ui.wechat.R.string.album_wechat_ui_title_send_count), selectCount, albumBundle.multipleMaxCount)
        }
        moveToPosition()
    }

    override fun onChangedViewPager(currentPos: Int, maxPos: Int) {
        album_wechat_preview_ui_bottom_prev.adapter?.notifyDataSetChanged()
        album_wechat_preview_ui_toolbar.title = getString(albumUiBundle.toolbarText) + "(" + currentPos + "/" + maxPos + ")"
        moveToPosition()
    }


    private fun moveToPosition() {
        val currentEntity = prevFragment.getCurrentItem()
        if (!currentEntity.isCheck) {
            return
        }
        val indexOf = prevFragment.getSelectEntity().indexOf(currentEntity)
        if (indexOf == -1) return

        val linearLayoutManager = album_wechat_preview_ui_bottom_prev.layoutManager as LinearLayoutManager
        val firstItem = linearLayoutManager.findFirstVisibleItemPosition()
        val lastItem = linearLayoutManager.findLastVisibleItemPosition()
        when {
            indexOf <= firstItem -> album_wechat_preview_ui_bottom_prev.scrollToPosition(indexOf)
            indexOf < lastItem -> {
                val top = album_wechat_preview_ui_bottom_prev.getChildAt(indexOf - firstItem).top
                album_wechat_preview_ui_bottom_prev.scrollBy(0, top)
            }
            else -> album_wechat_preview_ui_bottom_prev.scrollToPosition(indexOf)
        }
    }

    private fun initFragment() {
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            prevFragment = PrevFragment.newInstance(intent.extras.orEmpty())
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(com.album.ui.wechat.R.id.album_frame_preview, prevFragment, PrevFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(albumUiBundle.previewBackRefresh, false)
        super.onBackPressed()
    }
}