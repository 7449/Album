package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.text.safeToastExpand
import androidx.kotlin.expand.view.hideExpand
import androidx.kotlin.expand.view.showExpand
import androidx.kotlin.expand.widget.doOnAnimationEndExpand
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryConfig
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.compat.activity.galleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.finder.findFinder
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.types.ScanType
import com.gallery.ui.wechat.*
import com.gallery.ui.wechat.WeChatPrevArgs.Companion.putArgs
import com.gallery.ui.wechat.adapter.WeChatFinderAdapter
import com.gallery.ui.wechat.engine.AnimEngine
import com.gallery.ui.wechat.engine.displayGalleryThumbnails
import com.gallery.ui.wechat.engine.displayGalleryWeChat
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*

class GalleryWeChatActivity : GalleryCompatActivity(R.layout.gallery_activity_wechat_gallery), GalleryFinderAdapter.AdapterFinderListener {

    private val newFinderAdapter: WeChatFinderAdapter by lazy { WeChatFinderAdapter(uiConfig, this) }
    private val videoDuration: Int by lazy { uiGapConfig.getInt(WeChatConfig.GALLERY_WE_CHAT_VIDEO_DURATION, 300000) }
    private val videoDes: String by lazy { uiGapConfig.getString(WeChatConfig.GALLERY_WE_CHAT_VIDEO_DES, "全部视频") }
    private val videoList: ArrayList<ScanEntity> = arrayListOf()
    private val tempVideoList: ArrayList<ScanEntity> = arrayListOf()

    override val currentFinderName: String
        get() = galleryWeChatToolbarFinderText.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryWeChatFragment

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(WeChatConfig.GALLERY_WE_CHAT_VIDEO_ALL, videoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)

        tempVideoList.clear()
        tempVideoList.addAll(savedInstanceState?.getParcelableArrayList(WeChatConfig.GALLERY_WE_CHAT_VIDEO_ALL)
                ?: arrayListOf())
        videoList.clear()
        videoList.addAll(ArrayList(tempVideoList))

        galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }

        galleryWeChatFinder.adapter = newFinderAdapter
        galleryWeChatPrev.setOnClickListener {
            onStartPrevPage(ScanType.SCAN_NONE, 0,
                    option = WeChatPrevArgs(true, videoDuration, galleryWeChatFullImage.isChecked).putArgs(Bundle()),
                    cla = GalleryWeChatPrevActivity::class.java
            )
        }
        galleryWeChatToolbarSend.setOnClickListener { onGalleryResources(galleryFragment.selectEntities) }
        galleryWeChatToolbarFinder.setOnClickListener {
            if (finderList.isNullOrEmpty()) {
                onGalleryFinderEmpty()
                return@setOnClickListener
            }
            newFinderAdapter.updateFinder(finderList)
            galleryWeChatToolbarFinderIcon.animation?.let {
                if (it == rotateAnimationResult) {
                    showFinderActionView()
                } else {
                    hideFinderActionView()
                }
            } ?: showFinderActionView()
        }
        rotateAnimation.doOnAnimationEndExpand { AnimEngine.newInstance(galleryWeChatRoot.height).openAnim(galleryWeChatFinderRoot) }
        rotateAnimationResult.doOnAnimationEndExpand {
            val currentFragment = galleryFragment
            AnimEngine.newInstance(galleryWeChatRoot.height).closeAnimate(galleryWeChatFinderRoot) {
                if (finderList.find { it.isSelected }?.parent == currentFragment.parentId) {
                    return@closeAnimate
                }
                val find = finderList.find { it.parent == currentFragment.parentId }
                galleryWeChatToolbarFinderText.text = find?.bucketDisplayName
                //点击的是全部视频,更新fragment的parentId并直接调用scanMultipleSuccess走更新流程
                //否则使用parentId扫描数据
                //最后更新目录数据
                if (find?.parent == WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) {
                    currentFragment.parentId = WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT
                    currentFragment.scanMultipleSuccess(videoList)
                } else {
                    currentFragment.onScanGallery(find?.parent ?: ScanType.SCAN_ALL)
                }
                finderList.forEach { it.isSelected = it.parent == currentFragment.parentId }
                newFinderAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onGalleryCreated(fragment: Fragment, recyclerView: RecyclerView, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        super.onGalleryCreated(fragment, recyclerView, galleryBundle, savedInstanceState)
        val currentFragment = galleryFragment
        currentFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerView.layoutManager ?: return
                val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val position: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (dx == 0 && dy == 0) {
                    galleryWeChatTime.hideExpand()
                } else {
                    galleryWeChatTime.showExpand()
                }
                if (currentFragment.currentEntities.isNotEmpty()) {
                    currentFragment.currentEntities[if (position < 0) 0 else position].let { galleryWeChatTime.text = it.dateModified.formatTime() }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                galleryWeChatTime.postDelayed({ galleryWeChatTime.hideExpand() }, 1000)
            }
        })
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        val currentFragment = galleryFragment
        if (currentFragment.isScanAll) {
            videoList.clear()
            videoList.addAll(scanEntities.filter { it.isVideo })
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryConfig.sdName, galleryConfig.allName))
            scanEntities.find { it.isVideo }?.let { it ->
                finderList.add(1, it.copy(delegate = it.delegate.copy(parent = WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT, bucketDisplayName = videoDes), count = videoList.size))
            }
            finderList.firstOrNull()?.isSelected = true
        } else if (currentFragment.parentId == WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT && tempVideoList.isNotEmpty()) {
            //这里判断下，parentId 如果等于视频parentId且tempVideoList不为空的情况下则横竖屏切换的时候是全部视频，扫描的是 GALLERY_WE_CHAT_ALL_VIDEO_PARENT
            //数据肯定为空，这里再重新调用下 scanSuccess 赋值数据
            //为了避免重复调用的问题，tempVideoList需要clone后立马清空
            val arrayList = ArrayList(tempVideoList)
            tempVideoList.clear()
            currentFragment.scanMultipleSuccess(arrayList)
        }
        //每次扫描成功之后需要更新下activity的UI
        updateView()
    }

    /** 点击文件夹目录，更新parentId是因为[rotateAnimationResult]有用到 */
    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        galleryFragment.parentId = item.parent
        hideFinderActionView()
    }

    /** 预览页back返回 */
    override fun onResultBack(bundle: Bundle) {
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        updateView()
    }

    /** 预览页toolbar返回 */
    override fun onResultToolbar(bundle: Bundle) {
        onResultBack(bundle)
    }

    /** 预览页确定选择 */
    override fun onResultSelect(bundle: Bundle) {
        //https://github.com/7449/Album/issues/3
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        super.onResultSelect(bundle)
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, checkBox: TextView) {
        container.displayGalleryWeChat(width, height, galleryFragment.selectEntities, scanEntity, checkBox)
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryThumbnails(finderEntity)
    }

    /** 如果是全部视频parentId传递[ScanType.SCAN_ALL]否则传递当前[parentId] */
    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        onStartPrevPage(if (parentId == WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) ScanType.SCAN_ALL else parentId,
                position,
                if (parentId == WeChatConfig.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO else MediaStore.Files.FileColumns.MEDIA_TYPE_NONE,
                WeChatPrevArgs(false, videoDuration, galleryWeChatFullImage.isChecked).putArgs(),
                GalleryWeChatPrevActivity::class.java)
    }

    /** 刷新一下角标 */
    override fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, scanEntity)
        galleryFragment.notifyDataSetChanged()
    }

    /**
     * 点击之后判断是否是视频
     * 如果是并且时长超过了限制时长则提示视频过长
     * 如果是并且时长小于等于0则提示不能分享这种格式的视频
     * 否则更新[updateView]
     * 然后刷新当前选中item的状态
     * 因为选中的checkbox有数字显示，所以过滤出所有选中的数据刷新一下角标
     */
    override fun onChangedCheckBox(position: Int, scanEntity: ScanEntity) {
        val fragment = galleryFragment
        val selectEntities = fragment.selectEntities
        if (scanEntity.isVideo && scanEntity.duration > videoDuration) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_max_length).safeToastExpand(this)
        } else if (scanEntity.isVideo && scanEntity.duration <= 0) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_error).safeToastExpand(this)
        } else {
            updateView()
        }
        fragment.notifyItemChanged(position)
        if (scanEntity.isSelected) {
            return
        }
        fragment.currentEntities.mapIndexedNotNull { index, item -> if (item.isSelected) index else null }.forEach {
            fragment.notifyItemChanged(it)
        }
    }

    /** 更新顶部发送底部预览文字和状态 */
    @SuppressLint("SetTextI18n")
    private fun updateView() {
        val fragment = galleryFragment
        galleryWeChatToolbarSend.isEnabled = !fragment.selectEmpty
        galleryWeChatPrev.isEnabled = !fragment.selectEmpty
        galleryWeChatToolbarSend.text = uiConfig.selectText + if (fragment.selectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.multipleMaxCount})"
        galleryWeChatPrev.text = uiConfig.preViewText + if (fragment.selectEmpty) "" else "(${fragment.selectCount})"
    }

    /** 显示Finder */
    private fun showFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    /** 隐藏Finder */
    private fun hideFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    /** 选择图片,针对多选 */
    override fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(GalleryConfig.GALLERY_MULTIPLE_DATA, entities)
        bundle.putBoolean(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
        intent.putExtras(bundle)
        setResult(GalleryConfig.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

    /** 扫描到的文件目录为空 */
    private fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).safeToastExpand(this)
    }

}