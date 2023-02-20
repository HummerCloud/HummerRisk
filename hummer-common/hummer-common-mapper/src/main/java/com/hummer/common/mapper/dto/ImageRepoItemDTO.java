package com.hummer.common.mapper.dto;


import com.hummer.common.mapper.domain.ImageRepoItem;

import java.util.List;

public class ImageRepoItemDTO extends ImageRepoItem {

    private List<ImageRepoItemK8sDTO> imageRepoItemK8sDTOList;

    public List<ImageRepoItemK8sDTO> getImageRepoItemK8sDTOList() {
        return imageRepoItemK8sDTOList;
    }

    public void setImageRepoItemK8sDTOList(List<ImageRepoItemK8sDTO> imageRepoItemK8sDTOList) {
        this.imageRepoItemK8sDTOList = imageRepoItemK8sDTOList;
    }
}
