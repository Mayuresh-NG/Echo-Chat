package com.example.firebase.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.View.Activity.ChatActivity
import com.example.firebase.View.Adapter.UserAdapter
import com.example.firebase.viewmodel.ChatWindowViewModel

class ChatWindowFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val viewModel: ChatWindowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter { email ->
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        recyclerView.adapter = userAdapter

        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            userAdapter.submitList(users)
        })
    }
}
