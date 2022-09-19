package my.edu.tarc.mycontact

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import my.edu.tarc.mycontact.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ContactFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val contactViewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Enable menu item
        setHasOptionsMenu(true)

        //OR, for newer versions of IDE
        //val menuHost: MenuHost = requireActivity()
        //menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        Log.d("onCreateView", "FirstFragment")

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.action_profile -> {
                val navController = activity?.findNavController(R.id.nav_host_fragment_content_main)
                navController?.navigate(R.id.action_ContactFragment_to_ProfileFragment)
                return true
            }
            R.id.action_add->{
                val navController = activity?.findNavController(R.id.nav_host_fragment_content_main)
                navController?.navigate(R.id.action_ContactFragment_to_addContactFragment)
                return true
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_save).isVisible = false
        menu.findItem(R.id.action_delete).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d("onStart", "FirstFragment")
        super.onStart()
    }

    override fun onResume(){
        Log.d("onResume", "FirstFragment")
        super.onResume()

        //val contactAdapter = ContactAdapter(MainActivity.contactList)
        val contactAdapter = ContactAdapter(ContactAdapter.contactOnClickListener{
            //TODO: retrieve selected contact, pass it to the Edit Fragment
            contactViewModel.selectedContact = it
            contactViewModel.editMode = true
            findNavController().navigate(R.id.action_ContactFragment_to_addContactFragment)
        })

        contactViewModel.contactList.observe(viewLifecycleOwner){
            binding.textView3.isVisible = it.isEmpty()

            contactAdapter.setContact(it)

        }

        binding.recycleViewContact.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.recycleViewContact.adapter = contactAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}