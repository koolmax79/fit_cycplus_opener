package ru.koolmax.fitopener.presentation.ui.Main.DeviceList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.koolmax.fitopener.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DeviceListFragment : Fragment(R.layout.fragment_device_list) {
    private val viewModel by lazy { ViewModelProvider(this, DeviceListViewModelFactory(requireContext()))[DeviceListViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listView = view.findViewById<RecyclerView>(R.id.device_list)
        val adapter = DeviceDataAdapter()

        listView.adapter = adapter
        adapter.onItemClickListener = {
            val bundle = Bundle()
            bundle.putString("address", it.address)
            Navigation.findNavController(view).navigate(R.id.navigation_device, bundle)
        }

        viewModel.deviceList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.startScan()
    //        if (checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PERMISSION_GRANTED) {
//            viewModel.list()
//        } else {
//            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_SCAN), 1)
//        }

        //if (ActivityCompat.checkSelfPermission(
        //        requireContext(),
        //        Manifest.permission.BLUETOOTH_SCAN
        //    ) != PackageManager.PERMISSION_GRANTED
        //) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        //    return
        //}
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeviceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeviceListFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}