package com.album.ui.wechat.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.album.Album
import com.album.core.orEmpty
import com.album.core.scan.AlbumEntity
import com.album.ui.wechat.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.album_wechat_ui_finder.*

/**
 * @author y
 * @create 2019/3/11
 */
class AlbumWeChatUiFinder : BottomSheetDialogFragment() {

    companion object {
        const val OPTION = "Album:Ui.finder.option"
        fun newInstance(finderEntity: ArrayList<AlbumEntity>, fragmentManager: FragmentManager, tag: String): AlbumWeChatUiFinder {
            val albumWeChatUiFinder = AlbumWeChatUiFinder()
            albumWeChatUiFinder.arguments = Bundle().apply { putParcelableArrayList(OPTION, finderEntity) }
            albumWeChatUiFinder.show(fragmentManager, tag)
            return albumWeChatUiFinder
        }
    }

    lateinit var bundle: Bundle
    private lateinit var mActivity: FragmentActivity
    var onfinderAction: OnFinderActionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as? FragmentActivity ?: activity!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = arguments.orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.album_wechat_ui_finder, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val parcelableArrayList = arguments?.getParcelableArrayList<AlbumEntity>(AlbumWeChatUiFinder.OPTION)
                ?: ArrayList()
        album_wechat_ui_finder_view.setHasFixedSize(true)
        album_wechat_ui_finder_view.layoutManager = LinearLayoutManager(mActivity)
        album_wechat_ui_finder_view.adapter = object : RecyclerView.Adapter<FinderViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinderViewHolder {
                val finderView: View = LayoutInflater.from(parent.context).inflate(R.layout.album_wechat_item_finder, parent, false)
                val finderViewHolder = FinderViewHolder(finderView)
                finderView.setOnClickListener { v -> onfinderAction?.onFinderActionItemClick(v, finderViewHolder.adapterPosition, parcelableArrayList[finderViewHolder.adapterPosition]) }
                return finderViewHolder
            }

            override fun getItemCount(): Int = parcelableArrayList.size

            override fun onBindViewHolder(holder: FinderViewHolder, position: Int) {
                val albumEntity = parcelableArrayList[position]
                holder.appCompatTextView.text = String.format("%s(%s)", albumEntity.bucketDisplayName, albumEntity.count.toString())
                val imageView = Album.instance.albumImageLoader?.displayAlbumThumbnails(albumEntity, holder.frameLayout)
                imageView?.let { holder.frameLayout.addView(it) }
            }
        }
    }

    interface OnFinderActionListener {
        fun onFinderActionItemClick(view: View, position: Int, albumEntity: AlbumEntity)
    }

    class FinderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frameLayout: FrameLayout = itemView.findViewById(R.id.wechat_iv_album_finder_icon)
        var appCompatTextView: AppCompatTextView = itemView.findViewById(R.id.wechat_tv_album_finder_name)
    }
}