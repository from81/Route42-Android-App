package com.comp6442.groupproject.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp6442.groupproject.R;
import com.comp6442.groupproject.data.UserViewModel;
import com.comp6442.groupproject.data.model.Post;
import com.comp6442.groupproject.data.model.User;
import com.comp6442.groupproject.data.repository.PostRepository;
import com.comp6442.groupproject.ui.FirestorePostAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
  private static final String ARG_PARAM1 = "uid";
  private String uid;
  private UserViewModel viewModel;
  private RecyclerView recyclerView;
  private FirestorePostAdapter adapter;
  private LinearLayoutManager layoutManager;

  public FeedFragment() {
    // Required empty public constructor
  }

  public static FeedFragment newInstance(String param1) {
    Timber.i("New instance created with param %s", param1);
    FeedFragment fragment = new FeedFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      this.uid = getArguments().getString(ARG_PARAM1);
    }
    Timber.i("onCreate called with uid %s", uid);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_feed, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Timber.d("breadcrumb");

    if (savedInstanceState != null) {
      //Restore the fragment's state here
      Timber.i("Restoring fragment state");
      this.uid = savedInstanceState.getString("uid");
    }

    if (this.uid != null) {
      viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
      User user = viewModel.getLiveUser().getValue();

      assert user != null;

      Query query = PostRepository.getInstance().getVisiblePosts(user, 20);
      FirestoreRecyclerOptions<Post> posts = new FirestoreRecyclerOptions.Builder<Post>()
              .setQuery(query, Post.class)
              .build();

      adapter = new FirestorePostAdapter(posts);
      adapter.notifyDataSetChanged();
      layoutManager = new LinearLayoutManager(getActivity());
      recyclerView = view.findViewById(R.id.recycler_view);
      recyclerView.setLayoutManager(layoutManager);
      recyclerView.setAdapter(adapter);
      adapter.startListening();

      Timber.i("PostAdapter bound to RecyclerView with size %d", adapter.getItemCount());
      query.get().addOnSuccessListener(queryDocumentSnapshots -> Timber.i("%d items found", queryDocumentSnapshots.getDocuments().size()));

    } else {
      Timber.e("not signed in");
    }
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    Timber.d("breadcrumb");
  }

  @Override
  public void onStart() {
    super.onStart();
    Timber.d("breadcrumb");
    if (this.uid != null) Timber.i("Starting feed for uid = %s", this.uid);
  }

  @Override
  public void onStop() {
    super.onStop();
    Timber.d("breadcrumb");
    adapter.stopListening();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(ARG_PARAM1, this.uid);
    Timber.i("Saved instance state");
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Timber.d("breadcrumb");
  }

  /* onDetach() is always called after any Lifecycle state changes. */
  @Override
  public void onDetach() {
    super.onDetach();
    Timber.d("breadcrumb");
  }
}