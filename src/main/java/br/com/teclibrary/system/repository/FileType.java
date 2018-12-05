package br.com.teclibrary.system.repository;

public enum FileType {
    //    Image(".jpeg"), PDF(".pdf"), Text(".txt");
    Image {
        @Override
        public String toString() {
            return ".jpeg";
        }
    },
    PDF {
        @Override
        public String toString() {
            return ".pdf";
        }
    },
    Text {
        @Override
        public String toString() {
            return ".txt";
        }
    }
}
