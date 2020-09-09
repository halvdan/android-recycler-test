package com.example.myapplication.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ButtonBinding
import com.example.myapplication.databinding.InputBinding
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.databinding.TextBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MainAdapter(
                listOf(
                    FooView.Text("Foo"),
                    FooView.Input("Bar"),
                    FooView.Button("Submit", object : OnClickFoo {
                        override fun onClick(view: View) {
                            Toast.makeText(view.context, "Some text", Toast.LENGTH_LONG).show()
                        }
                    }),
                    FooView.Text("Bar"),
                    FooView.Button("Also Submit", object : OnClickFoo {
                        override fun onClick(view: View) {
                            Toast.makeText(view.context, "Also text", Toast.LENGTH_LONG).show()
                        }
                    }),
                    FooView.Input("Bar"),
                    FooView.Button("Launch Google", object : OnClickFoo {
                        override fun onClick(view: View) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://www.google.com")
                            startActivity(intent)
                        }
                    }),
                    FooView.Text("Baz"),
                    FooView.Button("Other screen (but same)", object : OnClickFoo {
                        override fun onClick(view: View) {
                            startActivity(Intent(view.context, MainActivity::class.java))
                        }
                    }),
                    FooView.Input("Bar"),
                    FooView.Text("Bar"),
                    FooView.Button("Submit"),
                    FooView.Text("Bar"),
                    FooView.Input("Bar"),
                    FooView.Input("Bar"),
                    FooView.Input("Bar"),
                    FooView.Input("Bar"),
                    FooView.Button("Submit"),
                    FooView.Text("Bar"),
                    FooView.Button("Submit"),
                    FooView.Text("Bar")
                )
            )
        }
    }

}

class MainAdapter(private val data: List<FooView>) :
    RecyclerView.Adapter<MainAdapter.FooViewHolder>() {
    sealed class FooViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Text(val binding: TextBinding) : FooViewHolder(binding.root)
        class Input(val binding: InputBinding) : FooViewHolder(binding.root)
        class Button(val binding: ButtonBinding) : FooViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooViewHolder {
        return when (viewType) {
            FooView.Text.viewType -> {
                val textView =
                    TextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FooViewHolder.Text(textView)
            }
            FooView.Input.viewType -> {
                val view = InputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FooViewHolder.Input(view)
            }
            FooView.Button.viewType -> {
                val view = ButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FooViewHolder.Button(view)
            }
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FooViewHolder, position: Int) {
        when (holder) {
            is FooViewHolder.Text -> holder.binding.content =
                (data[position] as FooView.Text).content
            is FooViewHolder.Input -> holder.binding.label =
                (data[position] as FooView.Input).label
            is FooViewHolder.Button -> {
                holder.binding.buttonName = (data[position] as FooView.Button).text
                holder.binding.listener = (data[position] as FooView.Button).onClick
            }
        }
    }
}

/*

mainBundle: [
    {
        view_id: 1,
        view_data: {
            content: "foo"
        }
    },
    {
        view_id: 2,
        view_data: {
            label: "this is a label"
        }
    },
    {
        view_id: 3,
        view_data: {
            text: "submit"
            on_click: { ... }
        }
    }
]

 */

sealed class FooView {
    abstract val id: Int

    class Text(val content: String) : FooView() {
        companion object {
            private const val _id = 1
            const val viewType = 1
        }

        override val id: Int = _id
    }

    class Input(val label: String) : FooView() {
        companion object {
            private const val _id = 2
            const val viewType = 2
        }

        override val id: Int = _id
    }

    class Button(val text: String, val onClick: OnClickFoo = NoOpOnClickFoo) : FooView() {
        companion object {
            private const val _id = 3
            const val viewType = 3
        }

        override val id: Int = _id
    }
}

interface OnClickFoo {
    fun onClick(view: View)
}

object NoOpOnClickFoo : OnClickFoo {
    override fun onClick(view: View) {
    }
}
