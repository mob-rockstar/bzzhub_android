package smartdev.bzzhub.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import smartdev.bzzhub.BzzApp

import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.activity.EventDetailActivity
import smartdev.bzzhub.ui.activity.MainActivity
import smartdev.bzzhub.ui.adapter.CommunityBubbleAdapter
import smartdev.bzzhub.ui.adapter.CommunityEventListAdapter
import smartdev.bzzhub.ui.adapter.CommunityFeedAdapter
import smartdev.bzzhub.ui.adapter.CommunityMainEventAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant

class CommunityEventFragment : BaseFragment(), CommunityBubbleAdapter.SelectedListener,CommunityMainEventAdapter.SelectedListener {

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
    private var eventList = ArrayList<CommunityEventListResponse.Result>()
    private var layoutAdd :View? = null

    private var communityEventAdapter: CommunityMainEventAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community_event, container, false)

        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerView = view.findViewById(R.id.recyclerView)
        // layoutAdd = view.findViewById(R.id.layout_add)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList

        initRecyclerViews()
        initTitle()
        getMainEvents()
        return view
    }


    fun initRecyclerViews(){
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.layoutManager = LinearLayoutManager(mContext)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == eventList.size - 1) {
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
        communityBubbleAdapter!!.selected = 3
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityEventFragment
    }

    override fun onBubbleSelected(position: Int) {
        when (position) {
            0 -> {
                   (activity!! as MainActivity).addFragment(CommunityFeedFragment(),false,
                           false,R.id.frameLayout,null)
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
          //      (activity!! as MainActivity).addFragment(CommunityEventFragment(),false,
          //              false,R.id.frameLayout,null)
            }
            4 ->{
                (activity!! as MainActivity).addFragment(CommunityFragment(),false,
                        false,R.id.frameLayout,null)
            }
        }
    }

    fun getEventList(){
        BzzApp.getBzHubRepository().getEventLists(pageId).subscribe(
                object : Observer<CommunityEventListResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: CommunityEventListResponse) {
                        isLoading = t.result == null || t.result.size != 10

                        eventList .clear()
                        if (t.result != null) {
                            eventList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }

                        if (communityEventAdapter == null){
                            communityEventAdapter = CommunityMainEventAdapter(activity!!, t.result)
                            communityEventAdapter!!.listener = this@CommunityEventFragment
                            recyclerView!!.adapter = communityEventAdapter
                        }else{
                            communityEventAdapter!!.addItems(eventList)
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

    override fun onSelected(event: CommunityEventListResponse.Result) {
       val  intent = Intent(currentActivity, EventDetailActivity::class.java)
        intent.putExtra("image",event.image)
        intent.putExtra("title",event.community)
        intent.putExtra("event_name",event.name)
        intent.putExtra("about_us",event.description)
        intent.putExtra("venue",event.city + ", " + event.country)
        startActivity(intent)
    }

    fun getMainEvents(){
        BzzApp.getBzHubRepository().getCommunityMainEvents(pageId).
                subscribe(object : Observer<CommunityEventListResponse> {
                    var mDispose: Disposable? = null
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }


                    override fun onNext(t: CommunityEventListResponse) {
                        isLoading = t.result == null || t.result.size != 10

                        eventList .clear()
                        if (t.result != null) {
                            eventList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }

                        if (communityEventAdapter == null){
                            communityEventAdapter = CommunityMainEventAdapter(activity!!, t.result)
                            communityEventAdapter!!.listener = this@CommunityEventFragment
                            recyclerView!!.adapter = communityEventAdapter
                        }else{
                            communityEventAdapter!!.addItems(eventList)
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}
