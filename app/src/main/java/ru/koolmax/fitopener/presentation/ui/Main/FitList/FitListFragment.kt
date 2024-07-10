package ru.koolmax.fitopener.presentation.ui.Main.FitList

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R
import ru.koolmax.fitopener.data.ble.DeviceRepository
import ru.koolmax.fitopener.data.db.FitRepository
import ru.koolmax.fitopener.data.db.FitDatabase
import ru.koolmax.fitopener.data.media.FileRepository
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModel
import ru.koolmax.fitopener.presentation.ui.Main.FitViewModelFactory

class FitListFragment : Fragment(R.layout.fragment_fit_list) {
    private val viewModel by lazy { ViewModelProvider(this, FitViewModelFactory(requireContext(),
        FileRepository(),
        FitRepository(FitDatabase.invoke(requireContext())),
        DeviceRepository()
    )
    )[FitViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val listView = view.findViewById<RecyclerView>(R.id.view_list)
//        val adapter = SessionDataAdapter(listOf()/*, viewModel8*/)
//
//        listView.adapter = adapter
//        adapter.onSessionClickListener = {
//            val bundle = Bundle()
//            bundle.putString("fit", it.fileName)
//            Navigation.findNavController(view).navigate(R.id.navigation_workout, bundle)
//        }
//
//        viewModel.FitSessionList.observe(viewLifecycleOwner) {
//            adapter.items = it
//            adapter.notifyDataSetChanged()
//        }
//        viewModel.getSessionList()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSessionList()
        //Log.i("FitOpener", "onResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<RecyclerView>(R.id.session_list)
        val adapter = SessionDataAdapter()

        listView.adapter = adapter
        adapter.onItemClickListener = {
            val bundle = Bundle()
            bundle.putString("fit", it.fileName)
            Navigation.findNavController(view).navigate(R.id.navigation_workout, bundle)
        }

        viewModel.fitSessionList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getSessionList()
    }
}