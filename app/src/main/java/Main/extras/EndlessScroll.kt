package Main.extras

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import extras.EndlessRecyclerOnScrollListener

class EndlessScroll {
    companion object {
        fun applyEndlessScroll(recyclerView : RecyclerView,
                               layoutMananger : LinearLayoutManager,
                               adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>,
                               dataList : ArrayList<Any?>)
        {
            var loadedMore = false

            recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutMananger, 0) {

                override fun onLoadMore(current_page: Int) {

                    if(!loadedMore){
                        loadedMore=true
                        try {

                            addNullItem(dataList)

                            val r = Runnable {

                                adapter.notifyItemInserted(dataList.size-1)

                            }

                            recyclerView.post(r)
                        }
                        catch (e:IllegalAccessException){
                            e.printStackTrace()
                        }

                    }

                }

                override fun onHide() {}

                override fun onShow() {}

                override fun onScrollStateChanged() {}

            })
        }

        private fun addNullItem(dataList : ArrayList<Any?>){
            dataList.add(null)
        }
    }
}