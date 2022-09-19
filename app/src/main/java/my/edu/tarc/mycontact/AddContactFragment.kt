package my.edu.tarc.mycontact

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import my.edu.tarc.mycontact.databinding.FragmentAddContactBinding
import my.edu.tarc.mycontact.entity.Contact


class AddContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    //Create a reference to view model
    private val contactViewModel:ContactViewModel by activityViewModels()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_add).isVisible = false
        menu.findItem(R.id.action_profile).isVisible = false
        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_upload).isVisible= false
        menu.findItem(R.id.action_delete).isVisible = contactViewModel.editMode
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentAddContactBinding.inflate( inflater, container, false)
        return binding.root
        return inflater.inflate(R.layout.fragment_add_contact2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(contactViewModel.editMode){
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit)
            binding.editTextPhone2.setText(contactViewModel.selectedContact.phone)
            binding.editTextTextPersonName.setText(contactViewModel.selectedContact.name)
            binding.editTextTextPersonName.requestFocus()
            binding.editTextPhone2.isEnabled = false
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_save -> {
                //todo: add contact record to a data storage
                val name = binding.editTextTextPersonName.text.toString()
                val phone = binding.editTextPhone2.text.toString()
                val newContact = Contact(name, phone)

                if(contactViewModel.editMode){
                    contactViewModel.update(newContact)
                    Toast.makeText(context, "Contact Edited",
                        Toast.LENGTH_SHORT).show()

                }else {
                    contactViewModel.insert(newContact)
                    Toast.makeText(context, "Contact Added",
                        Toast.LENGTH_SHORT).show()
                }


                //Return back to the First Fragment
                //val navController = activity?.findNavController(R.id.nav_host_fragment_content_main)
                //navController?.navigateUp()
                findNavController().navigateUp()

                true
            }
            R.id.action_delete -> {
                val alertDialog = AlertDialog.Builder(requireActivity())

                alertDialog.apply{
                    setTitle(getString(R.string.delete))
                    setMessage(getString(R.string.delete_message))
                    setPositiveButton(getString(R.string.delete)){_,_ ->
                        contactViewModel.delete(contactViewModel.selectedContact)
                        contactViewModel.selectedContact = Contact("","")
                        contactViewModel.editMode = false
                        findNavController().navigateUp()

                    }
                    setNegativeButton(android.R.string.cancel){alertDialog,_ ->
                        alertDialog.dismiss()
                    }
                }
                alertDialog.show()
                true

            }
        }
        return super.onOptionsItemSelected(item)
    }

}