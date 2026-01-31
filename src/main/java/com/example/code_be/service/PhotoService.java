package com.example.code_be.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.code_be.entity.Photo;
import com.example.code_be.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final Cloudinary cloudinary;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo findById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    public Page<Photo> findAll(Pageable pageable) {
        return photoRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public List<Photo> findByAlbum(String album) {
        return photoRepository.findByAlbumOrderByTakenAtDesc(album);
    }

    public List<String> findAllAlbums() {
        return photoRepository.findAllAlbums();
    }

    public void delete(Long id) {
        photoRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "ourlove/photos",
                        "resource_type", "auto"));
        return (String) uploadResult.get("secure_url");
    }

    public List<Photo> findOnThisDay(int month, int day) {
        return photoRepository.findByMonthAndDay(month, day);
    }
}
