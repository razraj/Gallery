package com.ramraj.work;

import com.ramraj.work.model.Image;

/**
 * Created by ramraj on 28/6/17.
 */

public class Events {

    public static class ImageSelectedEvent{
        private Image image;

        public ImageSelectedEvent(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }

    public static class OnItemClicked {
        private int position;

        public OnItemClicked(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class OnItemLongClicked {
        private int position;

        public OnItemLongClicked (int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class RefreshGallery{
        public RefreshGallery() {
        }
    }


}
