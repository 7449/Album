package develop.file.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryActivityGalleryBinding
import develop.file.gallery.compat.GalleryConfig
import develop.file.gallery.compat.activity.GalleryCompatActivity
import develop.file.gallery.compat.extensions.ActivityCompat.galleryFragment
import develop.file.gallery.compat.extensions.ActivityCompat.requireGalleryFragment
import develop.file.gallery.compat.extensions.ContextCompat.toast
import develop.file.gallery.compat.finder.GalleryFinderAdapter
import develop.file.gallery.compat.finder.findFinder
import develop.file.gallery.compat.widget.GalleryImageView
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.wechat.WeChatConfig
import develop.file.gallery.ui.wechat.adapter.WeChatFinderAdapter
import develop.file.gallery.ui.wechat.checkboxFImageResource
import develop.file.gallery.ui.wechat.colorWhite
import develop.file.gallery.ui.wechat.extension.AnimCompat.doOnAnimationEnd
import develop.file.gallery.ui.wechat.extension.AnimCompat.rotateAnimationSupport
import develop.file.gallery.ui.wechat.extension.AnimCompat.rotateAnimationSupport2
import develop.file.gallery.ui.wechat.extension.AnimCompat2
import develop.file.gallery.ui.wechat.extension.ResultCompat.weChatGalleryArgOrDefault
import develop.file.gallery.ui.wechat.extension.ViewCompat.createGalleryItem
import develop.file.gallery.ui.wechat.extension.ViewCompat.getCheckBoxView
import develop.file.gallery.ui.wechat.extension.ViewCompat.scrollView
import develop.file.gallery.ui.wechat.finderIcon
import develop.file.gallery.ui.wechat.maxVideoDuration
import develop.file.gallery.ui.wechat.rgb19
import develop.file.gallery.ui.wechat.rgb38
import develop.file.gallery.ui.wechat.size12
import develop.file.gallery.ui.wechat.size14
import develop.file.gallery.ui.wechat.textAllVideo
import develop.file.gallery.ui.wechat.textPrev
import develop.file.gallery.ui.wechat.textSend
import develop.file.media.Types

@SuppressLint("SetTextI18n")
internal class WeChatGalleryActivity : GalleryCompatActivity(),
    GalleryFinderAdapter.AdapterFinderListener {

    companion object {
        private const val VIDEO_ALL_LIST = "galleryWeChatVideoAll"
        private const val VIDEO_ALL_PARENT: Long = (-112).toLong()
    }

    //视频的数据
    //1.横竖屏保存一次
    //2.onCreate中获取新数据，扫描成功之后更新数据
    private val videoList: ArrayList<ScanEntity> = arrayListOf()

    //1.横竖屏切换从savedInstanceState中获取数据为[videoList]
    //2.扫描成功之后如果是判断当前parentId和VIDEO_ALL_PARENT相同且集合不为空则使用其数据更新UI
    //3.实在记不起来在什么样的场景下弄了个tempVideoList......
    private val tempVideoList: ArrayList<ScanEntity> = arrayListOf()

    private val rotateAnimation by lazy { rotateAnimationSupport }
    private val rotateAnimationResult by lazy { rotateAnimationSupport2 }
    private val binding by lazy { WechatGalleryActivityGalleryBinding.inflate(layoutInflater) }
    private val newFinderAdapter by lazy { WeChatFinderAdapter(this) }
    private val config by lazy { gapConfig.weChatGalleryArgOrDefault }

    override val currentFinderName get() = binding.galleryWeChatToolbarFinderText.text.toString()
    override val galleryFragmentId get() = R.id.galleryWeChatFragment

    private fun initViews() {
        window.statusBarColor = rgb38

        binding.galleryWeChatToolbar.setBackgroundColor(rgb38)
        binding.galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.galleryWeChatToolbarSend.textSize = size12
        binding.galleryWeChatToolbarSend.text = textSend
        binding.galleryWeChatToolbarSend.setOnClickListener {
            onGalleryResources(requireGalleryFragment.selectItem)
        }
        binding.galleryWeChatToolbarFinder.setOnClickListener {
            if (finderList.isEmpty()) return@setOnClickListener
            newFinderAdapter.updateFinder(finderList)
            binding.galleryWeChatToolbarFinderIcon.animation?.let {
                if (it == rotateAnimationResult) {
                    showFinderActionView()
                } else {
                    hideFinderActionView()
                }
            } ?: showFinderActionView()
        }
        binding.galleryWeChatToolbarFinderText.textSize = size14
        binding.galleryWeChatToolbarFinderText.setTextColor(colorWhite)
        binding.galleryWeChatToolbarFinderIcon.setImageResource(finderIcon)

        binding.galleryWeChatBottomView.setBackgroundColor(rgb19)
        binding.galleryWeChatPrev.text = textPrev
        binding.galleryWeChatPrev.textSize = size14
        binding.galleryWeChatPrev.setOnClickListener {
            startPrevPage(
                parentId = Types.Id.NONE,
                position = 0,
                gap = config.copy(
                    isPrev = true,
                    fullImageSelect = binding.galleryWeChatFullImage.isChecked
                ),
                cla = WeChatGalleryPrevActivity::class.java
            )
        }
        binding.galleryWeChatFullImage.setButtonDrawable(checkboxFImageResource)

        binding.galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }
        binding.galleryWeChatFinder.setBackgroundColor(rgb38)
        val divider = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.galleryWeChatFinder.addItemDecoration(divider)
        binding.galleryWeChatFinder.adapter = newFinderAdapter
        rotateAnimation.doOnAnimationEnd {
            AnimCompat2.newInstance(binding.galleryWeChatRoot.height)
                .openAnim(binding.galleryWeChatFinderRoot)
        }
        rotateAnimationResult.doOnAnimationEnd {
            val currentFragment = requireGalleryFragment
            AnimCompat2.newInstance(binding.galleryWeChatRoot.height)
                .closeAnimate(binding.galleryWeChatFinderRoot) {
                    if (finderList.find { it.isSelected }?.parent == currentFragment.parentId) return@closeAnimate
                    val find = finderList.find { it.parent == currentFragment.parentId }
                    updateFinderText(find?.bucketDisplayName.orEmpty())
                    //点击的是全部视频,更新fragment的parentId并直接调用scanMultipleSuccess走更新流程
                    //否则使用parentId扫描数据
                    //最后更新目录数据
                    if (find?.parent == VIDEO_ALL_PARENT) {
                        currentFragment.parentId = VIDEO_ALL_PARENT
                        currentFragment.scanMultipleSuccess(videoList)
                    } else {
                        currentFragment.onScanGallery(find?.parent ?: Types.Id.ALL)
                    }
                    newFinderAdapter.refreshFinder(currentFragment.parentId)
                }
        }
    }

    private fun showFinderActionView() {
        binding.galleryWeChatToolbarFinderIcon.clearAnimation()
        binding.galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    private fun hideFinderActionView() {
        binding.galleryWeChatToolbarFinderIcon.clearAnimation()
        binding.galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    private fun updateFinderText(text: String) {
        binding.galleryWeChatToolbarFinderText.text = text
    }

    private fun updateToolbarSend() {
        val fragment = galleryFragment ?: return
        binding.galleryWeChatToolbarSend.isEnabled = !fragment.isSelectEmpty
        binding.galleryWeChatToolbarSend.text =
            textSend + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.maxCount})"
    }

    private fun updateBottomPreView() {
        val fragment = galleryFragment ?: return
        binding.galleryWeChatPrev.isEnabled = !fragment.isSelectEmpty
        binding.galleryWeChatPrev.text =
            textPrev + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount})"
    }

    private fun getFullImageChecked(): Boolean {
        return binding.galleryWeChatFullImage.isChecked
    }

    private fun updateFullImageChecked(boolean: Boolean) {
        binding.galleryWeChatFullImage.isChecked = boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        updateFinderText(finderName)
        tempVideoList.clear()
        tempVideoList.addAll(
            savedInstanceState?.getParcelableArrayList(VIDEO_ALL_LIST)
                ?: arrayListOf()
        )
        videoList.clear()
        videoList.addAll(ArrayList(tempVideoList))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(VIDEO_ALL_LIST, videoList)
    }

    override fun onGalleryCreated(delegate: IScanDelegate, saveState: Bundle?) {
        delegate.rootView.setBackgroundColor(rgb38)
        scrollView(binding.galleryWeChatTime)
    }

    override fun onScanMultipleSuccess() {
        val currentFragment = requireGalleryFragment
        if (currentFragment.isScanAll) {
            //如果是扫描全部的则更新video和finder,然后为finder添加一个全部视频的item
            videoList.clear()
            videoList.addAll(currentFragment.allItem.filter { it.isVideo })
            finderList.clear()
            finderList.addAll(
                currentFragment.allItem.findFinder(
                    galleryConfig.sdNameAndAllName.first,
                    galleryConfig.sdNameAndAllName.second
                )
            )
            currentFragment.allItem.find { it.isVideo }?.let {
                finderList.add(
                    1,
                    it.copy(
                        delegate = it.delegate.copy(
                            parent = VIDEO_ALL_PARENT,
                            bucketDisplayName = textAllVideo
                        ),
                        count = videoList.size
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
        updateView()
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        requireGalleryFragment.parentId = item.parent
        hideFinderActionView()
    }

    override fun onResultBack(bundle: Bundle) {
        updateFullImageChecked(bundle.getBoolean(WeChatConfig.FULL_IMAGE))
        updateView()
    }

    override fun onResultToolbar(bundle: Bundle) {
        onResultBack(bundle)
    }

    override fun onResultSelect(bundle: Bundle) {
        updateFullImageChecked(bundle.getBoolean(WeChatConfig.FULL_IMAGE))
        super.onResultSelect(bundle)
    }

    override fun onGalleryFinderThumbnails(entity: ScanEntity, container: FrameLayout) {
        onDisplayFinderGallery(entity, container)
    }

    override fun onDisplayHomeGallery(
        width: Int,
        height: Int,
        entity: ScanEntity,
        container: FrameLayout
    ) {
        val weChatGalleryItem = container.createGalleryItem(width, height)
        weChatGalleryItem.update(entity)
        val checkBox = container.getCheckBoxView()
        if (entity.isSelected) {
            checkBox.text = (requireGalleryFragment.selectItem.indexOf(entity) + 1).toString()
        } else {
            checkBox.text = ""
        }
    }

    override fun onDisplayFinderGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(this).load(entity.uri).apply(
            RequestOptions().centerCrop()
        ).into(imageView)
        container.addView(imageView)
    }

    /** 如果是全部视频parentId传递[Types.Id.ALL]否则传递当前[parentId] */
    override fun onPhotoItemClick(entity: ScanEntity, position: Int, parentId: Long) {
        startPrevPage(
            parentId = if (parentId == VIDEO_ALL_PARENT) Types.Id.ALL else parentId,
            position = position,
            gap = config.copy(isPrev = false, fullImageSelect = getFullImageChecked()),
            singleType = if (parentId == VIDEO_ALL_PARENT) MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO else MediaStore.Files.FileColumns.MEDIA_TYPE_NONE,
            cla = WeChatGalleryPrevActivity::class.java
        )
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
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
    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
        val fragment = requireGalleryFragment
        val selectEntities = fragment.selectItem
        if (entity.isVideo && entity.duration > maxVideoDuration) {
            entity.isSelected = false
            selectEntities.remove(entity)
            toast(getString(R.string.wechat_gallery_select_video_max_length))
        } else if (entity.isVideo && entity.duration <= 0) {
            entity.isSelected = false
            selectEntities.remove(entity)
            toast(getString(R.string.wechat_gallery_select_video_error))
        } else {
            updateView()
        }
        fragment.notifyItemChanged(position)
        if (entity.isSelected) return
        fragment.allItem.mapIndexedNotNull { index, item -> if (item.isSelected) index else null }
            .forEach { fragment.notifyItemChanged(it) }
    }

    private fun updateView() {
        updateBottomPreView()
        updateToolbarSend()
    }

    override fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        intent.putExtras(
            bundleOf(
                GalleryConfig.RESULT_CODE_MULTIPLE_DATA.toString() to entities,
                WeChatConfig.FULL_IMAGE to getFullImageChecked()
            )
        )
        setResult(GalleryConfig.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

}