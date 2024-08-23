package smartdev.bzzhub.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.util.HalfSerializer.onNext
import io.reactivex.plugins.RxJavaPlugins.onSubscribe
import kotlinx.android.synthetic.main.item_news_feed.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import smartdev.bzzhub.BzzApp

import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.activity.MainActivity
import smartdev.bzzhub.ui.adapter.CommunityAdapter
import smartdev.bzzhub.ui.adapter.CommunityBubbleAdapter
import smartdev.bzzhub.ui.adapter.CommunityFeedAdapter
import smartdev.bzzhub.ui.adapter.NewsCommentAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class CommunityFeedFragment :  BaseFragment(), CommunityBubbleAdapter.SelectedListener,CommunityFeedAdapter.SelectedListener {

    var mContext: Context? = null
    var communityBubbleAdapter : CommunityBubbleAdapter? = null
    var titleList =   ArrayList<String>()

    var recyclerViewTab : RecyclerView? = null
    var recyclerView: RecyclerView? = null
    lateinit var tvFindCommunity : TextView
    lateinit var ivClose: ImageView

    private var selectorList = ArrayList<SelectorResponse.Result>()
    private var countries = ArrayList<CountryResponse.Result>()
    private var cities = ArrayList<CityResponse.Result>()
    private var allCities = ArrayList<CityResponse.Result>()
    private var cityAdapter: ArrayAdapter<CityResponse.Result>? = null

    private var pageId = 1
    private var isLoading = false
    private var loadMore = true
    private var communityFeedList = ArrayList<CommunityFeedResponse.Result>()
    private var layoutAdd :View? = null
    private var communityFeedAdapter: CommunityFeedAdapter? = null

    private var feedDetail: CommunityFeedDetailResponse.Result ?= null
    private var imageGroup : File? = null
    private var edittextFileName : EditText ? = null
    private var communityList = ArrayList<CommunityListResponse.Result>()
    private var groupId :Int = 0
    private var title = ""
    private var description = ""
    private var recyclerComment: RecyclerView? = null
    private var newsCommentAdapter: NewsCommentAdapter ?= null
    private var layoutComment: LinearLayout ?= null

    private lateinit var easyImage: EasyImage
    private var file: File? = null
    private var imageRequestCode = 100

    private var isDialogShowing  = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community_feed, container, false)

        // Define Easy Image
        easyImage =
                EasyImage.Builder(activity!!).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build()

        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerView = view.findViewById(R.id.recyclerView)
         layoutAdd = view.findViewById(R.id.layout_add)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList
        communityList = Constant.getInstance().communityList

        initRecyclerViews()
        initTitle()
        getCommunityFeedList()

        if (Constant.getInstance().loginFlag == 3){
            layoutAdd!!.visibility = View.GONE
        }else{
            layoutAdd!!.visibility = View.VISIBLE
        }
        layoutAdd!!.setOnClickListener {
            openAddNewsFeedDialog()
        }
        return  view
    }


    fun initRecyclerViews(){
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.layoutManager = GridLayoutManager(mContext,2)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == communityFeedList.size - 1) {
                        //bottom of list!
                        //      searchCompanyList()
                        isLoading = true
                        loadMore = true
                    }
                }
            }
        })
    }


    private fun initTitle(){
        titleList.add(activity!!.resources.getString(R.string.str_news_feed))
        titleList.add(activity!!.resources.getString(R.string.str_discussions))
        titleList.add(activity!!.resources.getString(R.string.str_media))
        titleList.add(activity!!.resources.getString(R.string.str_events))
        titleList.add(activity!!.resources.getString(R.string.str_community))

        communityBubbleAdapter = CommunityBubbleAdapter(context!!,titleList)
        communityBubbleAdapter!!.selected = 0
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityFeedFragment
    }

    override fun onBubbleSelected(position: Int) {
        when (position) {
            0 -> {
                //   (activity!! as MainActivity).addFragment()
            }
            1 -> {
                (activity!! as MainActivity).addFragment(CommunityDiscussionFragment(),false,
                        false,R.id.frameLayout,null)
            }
            2 -> {
                  (activity!! as MainActivity).addFragment(CommunityMediaFragment(),false,
                         false,R.id.frameLayout,null)
            }
            3 -> {
                     (activity!! as MainActivity).addFragment(CommunityEventFragment(),false,
                             false,R.id.frameLayout,null)
            }
            4 ->{
                (activity!! as MainActivity).addFragment(CommunityFragment(),false,
                        false,R.id.frameLayout,null)
            }
        }
    }

    fun getCommunityFeedList() {
        BzzApp.getBzHubRepository().getCommunityFeedList(pageId).subscribe(
                object : Observer<CommunityFeedResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: CommunityFeedResponse) {
                        isLoading = t.result == null || t.result.size != 10

                        communityFeedList .clear()
                        if (t.result != null) {
                            communityFeedList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }

                        if (communityFeedAdapter == null){
                            communityFeedAdapter = CommunityFeedAdapter(activity!!, t.result)
                            communityFeedAdapter!!.listener = this@CommunityFeedFragment
                            recyclerView!!.adapter = communityFeedAdapter
                        }else{
                            communityFeedAdapter!!.addFeeds(communityFeedList)
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                }
        )
    }

    override fun onSelected(feedId: Int) {
        if (!isDialogShowing){
            isDialogShowing = true
            getFeedDetail(feedId)
        }
    }

    private fun openAddNewsFeedDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_add_news_feed, null)
        dialog.window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(true)

        val edittextTitle = view.findViewById<EditText>(R.id.edittext_title)
        val edittextDescription = view.findViewById<EditText>(R.id.edittext_description)

        val spinnerGroup = view.findViewById<Spinner>(R.id.spinner_group)
        val groupSpinnerAdapter: ArrayAdapter<CommunityListResponse.Result> = ArrayAdapter<CommunityListResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, communityList)
        groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = groupSpinnerAdapter
        spinnerGroup.setSelection(communityList.size - 1)

        spinnerGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupId = communityList[position].communityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

         edittextFileName = view.findViewById<EditText>(R.id.edittext_file_name)
        edittextFileName!!.setOnClickListener {
             title = edittextTitle.text.toString()
             description = edittextDescription.text.toString()
            imageRequestCode = 100
            easyImage.openChooser(this)
        }

        val tvAdd = view.findViewById<TextView>(R.id.tv_add)
        tvAdd.setOnClickListener {
            if (groupId != 0 && title.isNotEmpty()  && description.isNotEmpty() && imageGroup != null){
                createNewsFeed()
                dialog.dismiss()
            }else{
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
            }
        }

        dialog.setCancelable(true)
        dialog.show()

    }

    var dialog: Dialog? = null
    private fun openFeedDetailDialog() {
         dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_news_feed_detail, null)
        dialog!!.window

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(view)
        Objects.requireNonNull(dialog!!.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lp = WindowManager.LayoutParams()
        val window = dialog!!.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp


        var ivCommunity = view.findViewById<ImageView>(R.id.iv_community)
        if (feedDetail?.image != null){
            Glide.with(currentActivity!!).load(feedDetail?.image).centerCrop().into(ivCommunity)
        }
        var tvTitle: TextView = view.findViewById(R.id.tv_feed_title)
        var tvMember: TextView = view.findViewById(R.id.tv_member)
        tvTitle.text = feedDetail?.description

         recyclerComment = view.findViewById(R.id.recyclerView_comment)
        recyclerComment!!.layoutManager = LinearLayoutManager(activity)


        var layoutSend : LinearLayout = view.findViewById(R.id.layout_send)
        tvMember.text = feedDetail?.title

        var editTextComment: EditText =  view.findViewById(R.id.edittext_comment)
        if (Constant.getInstance().loginFlag == 3){
            editTextComment.visibility = GONE

            layoutSend.visibility = GONE
        }
        val ivProfile = view.findViewById<ImageView>(R.id.iv_profile)

        layoutComment = view.findViewById(R.id.layout_comment)
        layoutSend.setOnClickListener {
            shareComment(editTextComment.text.toString())
            editTextComment.setText("")
        }

        dialog?.setOnDismissListener {
            isDialogShowing = false
        }

        dialog?.setOnCancelListener {
            isDialogShowing = false
        }

        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setCancelable(true)
        if (feedDetail != null){
            dialog!!.show()
        }else{
            isDialogShowing = false
        }
    }

    fun shareComment(comment: String){
        BzzApp.getBzHubRepository().writeFedComment(feedDetail!!.newFeedsId,Constant.getInstance().loginID,comment,
        Constant.getInstance().loginFlag).subscribe(
                object : Observer<SimpleResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(simplerResponse: SimpleResponse) {
                        if (simplerResponse.status && simplerResponse.code == 200) {
                            getCommentList(feedDetail!!.newFeedsId)
                        }
                    }

                    override fun onError(e: Throwable) {
                        this@CommunityFeedFragment.onError(e)
                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Profile Image Changed
        easyImage.handleActivityResult(requestCode, resultCode, data, activity!!, object :
                DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                if (imageRequestCode == 100) {
                    imageGroup = imageFiles.first().file
                    edittextFileName!!.setText(imageGroup!!.name!!)
                }
            }
        })
    }

    fun createNewsFeed(){
        BzzApp.getBzHubRepository().createNewsFeed(groupId,Constant.getInstance().loginID,title,description,
                Constant.getInstance().loginFlag, imageGroup).subscribe(
                object :Observer<SimpleResponse>{
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        if (t.code == 200){
                            (currentActivity as  MainActivity).showSnackBar((t.message),true)
                            communityFeedAdapter?.setItems(ArrayList())
                            getCommunityFeedList()
                        }else{
                            this@CommunityFeedFragment.onError(Exception(t.message))
                        }
                    }

                    override fun onError(e: Throwable) {
                        this@CommunityFeedFragment.onError(e)
                    }
                }
        )
    }

    fun getFeedDetail(feedID: Int){
        BzzApp.getBzHubRepository().getFeedDetails(feedID)
                .subscribe(object : Observer<CommunityFeedDetailResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }


                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: CommunityFeedDetailResponse) {
                        feedDetail = t.result
                        openFeedDetailDialog()
                        getCommentList(feedID)
                    }

                    override fun onError(e: Throwable) {
                        isDialogShowing = false
                    }
                })
    }

    fun getCommentList(feedID: Int){
        BzzApp.getBzHubRepository().getFeedComments(feedID,1)
                .subscribe(object : Observer<FeedCommentResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: FeedCommentResponse) {
                        if (t.status && t.code == 200){
                            newsCommentAdapter = NewsCommentAdapter(currentActivity!!,t.result)
                            recyclerComment!!.adapter = newsCommentAdapter
                            if (t.result.isEmpty()){
                                layoutComment!!.visibility = GONE
                            }else{
                                layoutComment!!.visibility = VISIBLE
                            }
                        }else{
                            layoutComment!!.visibility = GONE
                        }
                    }

                    override fun onError(e: Throwable) {

                    }
                });
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentActivity = context as MainActivity
    }
}
