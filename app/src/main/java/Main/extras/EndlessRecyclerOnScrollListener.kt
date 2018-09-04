package extras

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener(private val mLinearLayoutManager: LinearLayoutManager,
private val minimumDataCount: Int) : RecyclerView.OnScrollListener() {

    private var scrolledDistance = 0
    private var controlsVisible = true

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0

    private var current_page = 1

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        onScrollStateChanged()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)


        //hideToolbarOnScrollingDown(recyclerView,dy);
        visibleItemCount = recyclerView!!.childCount
        totalItemCount = mLinearLayoutManager.itemCount//list item count
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition() //list first visible item pos

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached
            if (totalItemCount >= minimumDataCount) {
                // Do something
                current_page++

                onLoadMore(current_page)

                loading = true
            }
        }

        /**
         * visibleItemCount = recyclerView.getChildCount();
         * totalItemCount = mLinearLayoutManager.getItemCount();
         * firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
         *
         *
         * if(visibleItemCount + firstVisibleItem >= totalItemCount){
         *
         * //end is reachhed
         * onLoadMore(current_page);
         *
         * }
         */
    }


    /*for hiding toolbar*/
    fun hideToolbarOnScrollingDown(recyclerView: RecyclerView, dy: Int) {
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        //show views if first item is first visible position and views are hidden
        if (firstVisibleItem == 0) {
            if (!controlsVisible) {
                onShow()
                controlsVisible = true
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide()
                controlsVisible = false
                scrolledDistance = 0
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow()
                controlsVisible = true
                scrolledDistance = 0
            }
        }

        if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
            scrolledDistance += dy
        }
    }


    abstract fun onLoadMore(current_page: Int)
    abstract fun onHide()
    abstract fun onShow()
    abstract fun onScrollStateChanged()

    companion object {
        var TAG = EndlessRecyclerOnScrollListener::class.java.simpleName

        /*for hiding toolbar*/
        private val HIDE_THRESHOLD = 20
        private val visibleThreshold = 0 // The minimum amount of items to have below your current scroll position before loading more.
        /*Minimum data count required to use load more*/
    }

}
