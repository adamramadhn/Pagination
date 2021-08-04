package com.example.pagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pagination.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersAdapter: Adapter
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usersAdapter = Adapter()


        with(binding.rvUsers) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                    val pastVisibleItem =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val total = (adapter as Adapter).itemCount
                    if (!isLoading && page < totalPage) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            page++
                            getUsers(false)
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
        binding.swipeRefresh.setOnRefreshListener(this)

        getUsers(false)
    }

    private fun getUsers(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) {
            binding.progressBar.visibility = View.VISIBLE
            val parameters = HashMap<String, String>()
            parameters["page"] = page.toString()
            RetrofitClient.instance.getUsers(parameters).enqueue(object : Callback<UsersResponses> {
                override fun onResponse(
                    call: Call<UsersResponses>,
                    response: Response<UsersResponses>
                ) {
                    totalPage = response.body()?.total_pages!!
                    val listResponse = response.body()?.data
                    if (listResponse != null) {
                        usersAdapter.addList(listResponse)
                    }
                    binding.progressBar.visibility = View.VISIBLE
                    isLoading = false
                    binding.swipeRefresh.isRefreshing = false
                    if(page == totalPage){
                        binding.progressBar.visibility = View.GONE
                    }else{
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<UsersResponses>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onRefresh() {
        usersAdapter.clear()
        page = 1
        getUsers(false)
    }
}