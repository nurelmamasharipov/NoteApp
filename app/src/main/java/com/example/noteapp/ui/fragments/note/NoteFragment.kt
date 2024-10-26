import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.ui.fragments.note.NoteFragmentDirections
import com.example.noteapp.ui.intetface.OnClickItem
import com.example.noteapp.utils.PreferenceHelper
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

class NoteFragment : Fragment(), OnClickItem {

    private lateinit var binding: FragmentNoteBinding
    private val noteAdapter = NoteAdapter(this, this)
    private val sharedPreferences = PreferenceHelper()
    private var layoutManager = true

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Разрешение на уведомления предоставлено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        requestNotificationPermission()
        initialize()
        setupListener()
        getData()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun initialize() = with(binding) {
        sharedPreferences.init(requireContext())
        if (sharedPreferences.isLinearLayout()) {
            rvNote.layoutManager = LinearLayoutManager(context)
            btnChange.setImageResource(R.drawable.baseline_widgets_24)
        } else {
            rvNote.layoutManager = GridLayoutManager(context, 2)
            btnChange.setImageResource(R.drawable.grid)
        }
        rvNote.adapter = noteAdapter
    }

    private fun setupListener() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.noteDetailFragment)
        }
        binding.btnChange.setOnClickListener {
            if(noteAdapter.currentList.isNotEmpty()) {
                layoutManager = !layoutManager
                if (layoutManager) {
                    sharedPreferences.layoutManager = true
                    binding.rvNote.layoutManager = LinearLayoutManager(context)
                    binding.btnChange.setImageResource(R.drawable.baseline_widgets_24)
                } else {
                    sharedPreferences.layoutManager = false
                    binding.rvNote.layoutManager = GridLayoutManager(context, 2)
                    binding.btnChange.setImageResource(R.drawable.grid)
                }
            }
        }
    }

    private fun getData() {
        App.appDataBase?.noteDao()?.getAll()?.observe(viewLifecycleOwner) { listNote ->
            noteAdapter.submitList(listNote)
        }
    }

    override fun OnLongClick(noteModel: NoteModel) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle("Удалить заметку?")
            setPositiveButton("Удалить") { _, _ ->
                App.appDataBase?.noteDao()?.deleteNote(noteModel)
                getData()
            }
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
        builder.create()
    }

    override fun onClick(noteModel: NoteModel) {
        val action = NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(noteModel.id)
        findNavController().navigate(action)
    }
}