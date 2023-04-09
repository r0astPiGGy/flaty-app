package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImageStorage {

    CompletableFuture<Void> saveImageByUri(byte[] image, String uri);

    CompletableFuture<Void> updateImageByUri(byte[] image, String uri);

    CompletableFuture<Void> deleteImageByUri(String uri);

    CompletableFuture<Void> deleteImagesByUri(List<String> uris);

    LiveData<byte[]> getImageByUri(String uri);

}
