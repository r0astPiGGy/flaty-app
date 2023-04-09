package com.rodev.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.rodev.flatyapp.data.ImageStorage;

public class ImageStorageImpl implements ImageStorage {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public StorageReference db() {
        return storage.getReference();
    }

    @Override
    public CompletableFuture<Void> saveImageByUri(byte[] image, String uri) {
        return updateImageByUri(image, uri);
    }

    @Override
    public CompletableFuture<Void> updateImageByUri(byte[] image, String uri) {
        CompletableFuture<Void> task = new CompletableFuture<>();

        Objects.requireNonNull(image);

        db().child(uri)
                .putBytes(image)
                .addOnSuccessListener(t -> task.complete(null))
                .addOnFailureListener(task::completeExceptionally);

        return task;
    }

    @Override
    public CompletableFuture<Void> deleteImageByUri(String uri) {
        CompletableFuture<Void> task = new CompletableFuture<>();

        db().child(uri)
                .delete()
                .addOnSuccessListener(t -> task.complete(null))
                .addOnFailureListener(task::completeExceptionally);

        return task;
    }

    @Override
    public CompletableFuture<Void> deleteImagesByUri(List<String> uris) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        db().listAll().addOnSuccessListener(listResult -> {
            List<StorageReference> refs = listResult.getItems()
                    .stream()
                    .filter(r -> {
                        String uri = r.getName();
                        return uris.contains(uri);
                    })
                    .collect(Collectors.toList());

            if(refs.isEmpty()) {
                completableFuture.complete(null);
                return;
            }

            refs.forEach(storageReference -> {
                storageReference
                        .delete()
                        .addOnSuccessListener(completableFuture::complete)
                        .addOnFailureListener(completableFuture::completeExceptionally);
            });

        }).addOnFailureListener(completableFuture::completeExceptionally);

        return completableFuture;
    }

    @Override
    public LiveData<byte[]> getImageByUri(String uri) {
        MutableLiveData<byte[]> task = new MutableLiveData<>();

        long maxDownloadSize = 1024 * 1024; // 1 megabyte

        db().child(uri)
                .getBytes(maxDownloadSize)
                .addOnSuccessListener(task::postValue)
                .addOnFailureListener(Throwable::printStackTrace);

        return task;
    }
}
