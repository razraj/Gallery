package com.ramraj.work;

import com.ramraj.work.model.Image;

/**
 * Created by ramraj on 28/6/17.
 */

public class Events {
    public static class ImageEditSelectedEvent{
        private Image image;

        public ImageEditSelectedEvent(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }

    public static class ImageSelectedEvent{
        private Image image;

        public ImageSelectedEvent(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }
}
