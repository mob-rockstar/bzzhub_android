package smartdev.bzzhub.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import pl.aprilapps.easyphotopicker.EasyImage
import smartdev.bzzhub.BzzApp

import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.activity.MainActivity
import smartdev.bzzhub.ui.adapter.CommunityBubbleAdapter
import smartdev.bzzhub.ui.adapter.CommunityDiscussionAdapter
import smartdev.bzzhub.ui.adapter.CommunityFeedAdapter
import smartdev.bzzhub.ui.adapter.DiscussionCommentAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant
import java.util.*
import kotlin.collections.ArrayList

class CommunityDiscussionFragment : BaseFragment() , CommunityBubbleAdapter.SelectedListener, CommunityDiscussionAdapter.SelectedListener{

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
    private var commentPageId = 1
    private var isLoading = false
    private var loadMore = true
    private var discussionList = ArrayList<DiscussionListResponse.Result>()
    private var layoutAdd :View? = null
    private var communityDiscussionAdapter: CommunityDiscussionAdapter? = null
    private var communityList = ArrayList<CommunityListResponse.Result>()
    private var groupId :Int = 0
    private var title = ""
    private var description = ""

    private var recyclerViwe: RecyclerView?= null
    private var adapter: DiscussionCommentAdapter? = null
    private var discussionID: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community_discussion, container, false)


        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerView = view.findViewById(R.id.recyclerView)
        layoutAdd = view.findViewById(R.id.layout_add)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList
        communityList = Constant.getInstance().communityList
        if (Constant.getInstance().loginFlag == 3){
            layoutAdd?.visibility = GONE
        }
        layoutAdd?.setOnClickListener {
            openAddNewsFeedDialog()
        }
        initRecyclerViews()
        initTitle()
        getCommunityDiscussionList()
        return view

    }

    fun initRecyclerViews(){
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.layoutManager = GridLayoutManager(mContext,2)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == discussionList.size - 1) {
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
        communityBubbleAdapter!!.selected = 1
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityDiscussionFragment
    }

    override fun onBubbleSelected(position: Int) {
        when (position) {
            0 -> {
                   (activity!! as MainActivity).addFragment(CommunityFeedFragment(),false,
                                         false,R.id.frameLayout,null)
            }
            1 -> {
          //      (activity!! as MainActivity).addFragment(CommunityDiscussionFragment(),false,
          //              false,R.id.frameLayout,null)
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

    fun getCommunityDiscussionList() {
        BzzApp.getBzHubRepository().getDiscussionList(pageId).subscribe(
                object : Observer<DiscussionListResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: DiscussionListResponse) {
                        isLoading = t.result == null || t.result.size != 10

                        discussionList .clear()
                        if (t.result != null) {
                            discussionList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }
                        if (communityDiscussionAdapter == null){
                            communityDiscussionAdapter = CommunityDiscussionAdapter(activity!!, t.result)
                            communityDiscussionAdapter!!.listener = this@CommunityDiscussionFragment
                            recyclerView!!.adapter = communityDiscussionAdapter
                        }else{
                            communityDiscussionAdapter!!.addItems(discussionList)
                            recyclerView!!.adapter = communityDiscussionAdapter
                            communityDiscussionAdapter!!.listener = this@CommunityDiscussionFragment
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


    private fun openAddNewsFeedDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_add_thread, null)
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

        val tvAdd = view.findViewById<TextView>(R.id.tv_add)
        tvAdd.setOnClickListener {
            title = edittextTitle.text.toString()
            description = edittextDescription.text.toString()
            if (groupId != 0 && title.isNotEmpty()  && description.isNotEmpty() ){
                createThread()
                dialog.dismiss()
            }else{
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
            }
        }

        dialog.setCancelable(true)
        dialog.show()
    }

    fun createThread(){
        BzzApp.getBzHubRepository().createDiscussion(
                groupId,title,description,Constant.getInstance().loginFlag,Constant.getInstance().loginID
        ).subscribe(
                object : Observer<SimpleResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        communityDiscussionAdapter?.setItems(ArrayList())
                        pageId = 1
                        getCommunityDiscussionList()
                    }

                    override fun onError(e: Throwable) {

                    }
                }
        )
    }

    override fun onSelected(mDiscussionID: Int) {
   //     if (Constant.getInstance().loginFlag != 3){
            discussionID = mDiscussionID
            threadCommentDialog()
            getDiscussionReplyList(mDiscussionID)
    //    }
    }

    fun getDiscussionReplyList(discussionId: Int){
        BzzApp.getBzHubRepository().getDiscussionReply(commentPageId,discussionId)
                .subscribe(object : Observer<DiscussionCommentResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }


                    override fun onNext(t: DiscussionCommentResponse) {
                        if (t.status && t.code == 200){
                            adapter = DiscussionCommentAdapter(currentActivity,t.result)
                            recyclerView?.adapter = adapter
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    fun threadCommentDialog(){
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_write_thread_comment, null)
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

        val edittextComment = view.findViewById<EditText>(R.id.edittext_comment)
        val layoutSend = view.findViewById<LinearLayout>(R.id.layout_send)

        if (Constant.getInstance().loginFlag == 3){
            edittextComment.visibility = GONE

            layoutSend.visibility = GONE
        }
        layoutSend.setOnClickListener{
                createDiscussionReply(edittextComment.text.toString().trim())
            edittextComment.setText("")
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(currentActivity!!)

        dialog.setCancelable(true)
        dialog.show()
    }

    fun createDiscussionReply(title:String){
        BzzApp.getBzHubRepository().createDiscussionReply(discussionID,title,Constant.getInstance().loginID,Constant.getInstance().loginFlag)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }


                    override fun onNext(t: SimpleResponse) {
                        getDiscussionReplyList(discussionID)
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

}
