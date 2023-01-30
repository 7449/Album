package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.GalleryConfig
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.compat.extensions.requireGalleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.finder.findFinder
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.hide
import com.gallery.core.extensions.show
import com.gallery.core.extensions.toast
import com.gallery.scan.Types
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.adapter.WeChatFinderAdapter
import com.gallery.ui.wechat.args.WeChatGalleryBundle
import com.gallery.ui.wechat.databinding.WechatGalleryActivityGalleryBinding
import com.gallery.ui.wechat.extension.*
import com.gallery.ui.wechat.widget.WeChatGalleryItem

class WeChatGalleryActivity : GalleryCompatActivity(), GalleryFinderAdapter.AdapterFinderListener {

    companion object {
        private const val VIDEO_ALL_LIST = "galleryWeChatVideoAll"
        private const val VIDEO_ALL_PARENT: Long = (-112).toLong()
    }

    private val rotateAnimation: RotateAnimation by lazy {
        RotateAnimation(
            0.toFloat(),
            180.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5.toFloat()
        ).apply {
            interpolator = LinearInterpolator()
            duration = 200
            fillAfter = true
        }
    }
    private val rotateAnimationResult: RotateAnimation by lazy {
        RotateAnimation(
            180.toFloat(),
            360.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5.toFloat()
        ).apply {
            interpolator = LinearInterpolator()
            duration = 200
            fillAfter = true
        }
    }

    private val binding: WechatGalleryActivityGalleryBinding by lazy {
        WechatGalleryActivityGalleryBinding.inflate(layoutInflater)
    }
    private val newFinderAdapter: WeChatFinderAdapter by lazy {
        WeChatFinderAdapter(
            uiGalleryBundle,
            this
        )
    }
    private val uiGalleryBundle: WeChatGalleryBundle by lazy { gapConfig.weChatGalleryArgOrDefault }

    //视频的数据
    //1.横竖屏保存一次
    //2.onCreate中获取新数据，扫描成功之后更新数据
    private val videoList: ArrayList<ScanEntity> = arrayListOf()

    //1.横竖屏切换从savedInstanceState中获取数据为[videoList]
    //2.扫描成功之后如果是判断当前parentId和VIDEO_ALL_PARENT相同且集合不为空则使用其数据更新UI
    //3.实在记不起来在什么样的场景下弄了个tempVideoList......
    private val tempVideoList: ArrayList<ScanEntity> = arrayListOf()

    override val currentFinderName: String
        get() = binding.galleryWeChatToolbarFinderText.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryWeChatFragment

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(VIDEO_ALL_LIST, videoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = uiGalleryBundle.statusBarColor
        binding.galleryWeChatToolbar.setBackgroundColor(uiGalleryBundle.toolbarBackground)

        binding.galleryWeChatFinder.layoutManager = LinearLayoutManager(this)
        binding.galleryWeChatFinder.setBackgroundColor(uiGalleryBundle.finderItemBackground)
        binding.galleryWeChatFinder.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )

        binding.galleryWeChatBottomView.setBackgroundColor(uiGalleryBundle.bottomViewBackground)

        binding.galleryWeChatPrev.text = uiGalleryBundle.preViewText
        binding.galleryWeChatPrev.textSize = uiGalleryBundle.preViewTextSize

        binding.galleryWeChatToolbarSend.textSize = uiGalleryBundle.selectTextSize
        binding.galleryWeChatToolbarSend.text = uiGalleryBundle.selectText
        binding.galleryWeChatFullImage.setButtonDrawable(R.drawable.wechat_gallery_selector_gallery_full_image_item_check)

        binding.galleryWeChatToolbarFinderText.textSize = uiGalleryBundle.finderTextSize
        binding.galleryWeChatToolbarFinderText.setTextColor(uiGalleryBundle.finderTextColor)
        binding.galleryWeChatToolbarFinderIcon.setImageResource(uiGalleryBundle.finderTextCompoundDrawable)
        binding.galleryWeChatToolbarFinderText.text = finderName

        tempVideoList.clear()
        tempVideoList.addAll(
            savedInstanceState?.getParcelableArrayList(VIDEO_ALL_LIST)
                ?: arrayListOf()
        )
        videoList.clear()
        videoList.addAll(ArrayList(tempVideoList))

        binding.galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }

        binding.galleryWeChatFinder.adapter = newFinderAdapter
        binding.galleryWeChatPrev.setOnClickListener {
            startPrevPage(
                parentId = Types.Id.NONE,
                position = 0,
                gap = uiGalleryBundle.copy(
                    isPrev = true,
                    fullImageSelect = binding.galleryWeChatFullImage.isChecked
                ),
                cla = WeChatGalleryPrevActivity::class.java
            )
        }
        binding.galleryWeChatToolbarSend.setOnClickListener {
            onGalleryResources(requireGalleryFragment.selectItem)
        }
        binding.galleryWeChatToolbarFinder.setOnClickListener {
            if (finderList.isEmpty()) {
                getString(R.string.wechat_gallery_finder_empty).toast(this)
                return@setOnClickListener
            }
            newFinderAdapter.updateFinder(finderList)
            binding.galleryWeChatToolbarFinderIcon.animation?.let {
                if (it == rotateAnimationResult) {
                    showFinderActionView()
                } else {
                    hideFinderActionView()
                }
            } ?: showFinderActionView()
        }
        rotateAnimation.doOnAnimationEndExpand {
            AnimExtension.newInstance(binding.galleryWeChatRoot.height)
                .openAnim(binding.galleryWeChatFinderRoot)
        }
        rotateAnimationResult.doOnAnimationEndExpand {
            val currentFragment = requireGalleryFragment
            AnimExtension.newInstance(binding.galleryWeChatRoot.height)
                .closeAnimate(binding.galleryWeChatFinderRoot) {
                    if (finderList.find { it.isSelected }?.parent == currentFragment.parentId) {
                        //如果点击的是当前选中的目录则return掉
                        return@closeAnimate
                    }
                    val find = finderList.find { it.parent == currentFragment.parentId }
                    binding.galleryWeChatToolbarFinderText.text = find?.bucketDisplayName
                    //点击的是全部视频,更新fragment的parentId并直接调用scanMultipleSuccess走更新流程
                    //否则使用parentId扫描数据
                    //最后更新目录数据
                    if (find?.parent == VIDEO_ALL_PARENT) {
                        currentFragment.parentId = VIDEO_ALL_PARENT
                        currentFragment.scanMultipleSuccess(videoList)
                    } else {
                        currentFragment.onScanGallery(find?.parent ?: Types.Id.ALL)
                    }
                    finderList.forEach { it.isSelected = it.parent == currentFragment.parentId }
                    newFinderAdapter.notifyDataSetChanged()
                }
        }
    }

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: com.gallery.core.GalleryConfigs,
        savedInstanceState: Bundle?
    ) {
        //初始化布局
        delegate.rootView.setBackgroundColor(uiGalleryBundle.galleryRootBackground)
        val currentFragment = requireGalleryFragment
        //滑动的时间提示View
        currentFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerView.layoutManager ?: return
                val layoutManager: LinearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager
                val position: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (dx == 0 && dy == 0) {
                    binding.galleryWeChatTime.hide()
                } else {
                    binding.galleryWeChatTime.show()
                }
                if (currentFragment.allItem.isNotEmpty()) {
                    currentFragment.allItem[if (position < 0) 0 else position].let {
                        binding.galleryWeChatTime.text = it.dateModified.formatTime()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.galleryWeChatTime.postDelayed(
                    { binding.galleryWeChatTime.hide() },
                    1000
                )
            }
        })
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        val currentFragment = requireGalleryFragment
        if (currentFragment.isScanAll) {
            //如果是扫描全部的则更新video和finder,然后为finder添加一个全部视频的item
            videoList.clear()
            videoList.addAll(scanEntities.filter { it.isVideo })
            finderList.clear()
            finderList.addAll(
                scanEntities.findFinder(
                    galleryConfig.sdNameAndAllName.first,
                    galleryConfig.sdNameAndAllName.second
                )
            )
            scanEntities.find { it.isVideo }?.let { it ->
                finderList.add(
                    1,
                    it.copy(
                        delegate = it.delegate.copy(
                            parent = VIDEO_ALL_PARENT,
                            bucketDisplayName = uiGalleryBundle.videoAllFinderName
                        ), count = videoList.size
                    )
                )
            }
            finderList.firstOrNull()?.isSelected = true
        } else if (currentFragment.parentId == VIDEO_ALL_PARENT && tempVideoList.isNotEmpty()) {
            //如果当前parentId等于视频parentId且tempVideoList不为空的情况下则横竖屏切换的时候是全部视频，
            //扫描的是VIDEO_ALL_PARENT
            //数据肯定为空，这里再重新调用下scanMultipleSuccess赋值数据
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
        requireGalleryFragment.parentId = item.parent
        hideFinderActionView()
    }

    /** 预览页back返回 */
    override fun onResultBack(bundle: Bundle) {
        binding.galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatConfig.FULL_IMAGE)
        updateView()
    }

    /** 预览页toolbar返回 */
    override fun onResultToolbar(bundle: Bundle) {
        onResultBack(bundle)
    }

    /** 预览页确定选择 */
    override fun onResultSelect(bundle: Bundle) {
        //https://github.com/7449/Album/issues/3
        binding.galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatConfig.FULL_IMAGE)
        super.onResultSelect(bundle)
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    @SuppressLint("SetTextI18n")
    override fun onDisplayGallery(
        width: Int,
        height: Int,
        scanEntity: ScanEntity,
        container: FrameLayout,
        checkBox: TextView
    ) {
        container.removeAllViews()
        val weChatGalleryItem = WeChatGalleryItem(container.context)
        weChatGalleryItem.update(scanEntity)
        checkBox.gravity = Gravity.CENTER
        checkBox.setTextColor(Color.WHITE)
        if (scanEntity.isSelected) {
            checkBox.text = (requireGalleryFragment.selectItem.indexOf(scanEntity) + 1).toString()
        } else {
            checkBox.text = ""
        }
        Glide.with(this).load(scanEntity.uri).apply(
            RequestOptions().centerCrop()
                .override(width, height)
        ).into(weChatGalleryItem.imageView)
        container.addView(weChatGalleryItem, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(this).load(finderEntity.uri).apply(
            RequestOptions().centerCrop()
        ).into(imageView)
        container.addView(imageView)
    }

    /** 如果是全部视频parentId传递[Types.Id.ALL]否则传递当前[parentId] */
    override fun onPhotoItemClick(
        context: Context,
        bundle: com.gallery.core.GalleryConfigs,
        scanEntity: ScanEntity,
        position: Int,
        parentId: Long
    ) {
        startPrevPage(
            parentId = if (parentId == VIDEO_ALL_PARENT) Types.Id.ALL else parentId,
            position = position,
            gap = uiGalleryBundle.copy(
                isPrev = false,
                fullImageSelect = binding.galleryWeChatFullImage.isChecked
            ),
            scanSingleType = if (parentId == VIDEO_ALL_PARENT) MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO else MediaStore.Files.FileColumns.MEDIA_TYPE_NONE,
            cla = WeChatGalleryPrevActivity::class.java
        )
    }

    /** 刷新一下角标 */
    override fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, scanEntity)
        requireGalleryFragment.notifyDataSetChanged()
    }

    /**
     * 点击之后判断是否是视频
     * 如果是并且时长超过了限制时长则提示视频过长
     * 如果是并且时长小于等于0则提示不能分享这种格式的视频
     * 否则更新[updateView]
     * 然后刷新当前选中item的状态
     * 因为选中的checkbox有数字显示，所以过滤出所有选中的数据刷新一下角标
     */
    override fun onSelectItemChanged(position: Int, scanEntity: ScanEntity) {
        val fragment = requireGalleryFragment
        val selectEntities = fragment.selectItem
        if (scanEntity.isVideo && scanEntity.duration > uiGalleryBundle.videoMaxDuration) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.wechat_gallery_select_video_max_length).toast(this)
        } else if (scanEntity.isVideo && scanEntity.duration <= 0) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.wechat_gallery_select_video_error).toast(this)
        } else {
            updateView()
        }
        fragment.notifyItemChanged(position)
        if (scanEntity.isSelected) {
            return
        }
        fragment.allItem.mapIndexedNotNull { index, item -> if (item.isSelected) index else null }
            .forEach {
                fragment.notifyItemChanged(it)
            }
    }

    /** 更新顶部发送底部预览文字和状态 */
    @SuppressLint("SetTextI18n")
    private fun updateView() {
        val fragment = requireGalleryFragment
        binding.galleryWeChatToolbarSend.isEnabled = !fragment.isSelectEmpty
        binding.galleryWeChatPrev.isEnabled = !fragment.isSelectEmpty
        binding.galleryWeChatToolbarSend.text =
            uiGalleryBundle.selectText + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.maxCount})"
        binding.galleryWeChatPrev.text =
            uiGalleryBundle.preViewText + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount})"
    }

    /** 显示Finder */
    private fun showFinderActionView() {
        binding.galleryWeChatToolbarFinderIcon.clearAnimation()
        binding.galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    /** 隐藏Finder */
    private fun hideFinderActionView() {
        binding.galleryWeChatToolbarFinderIcon.clearAnimation()
        binding.galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    /** 选择图片,针对多选 */
    override fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        intent.putExtras(
            bundleOf(
                GalleryConfig.GALLERY_MULTIPLE_DATA to entities,
                WeChatConfig.FULL_IMAGE to binding.galleryWeChatFullImage.isChecked
            )
        )
        setResult(GalleryConfig.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

}