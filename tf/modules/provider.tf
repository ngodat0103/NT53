provider "google" {
  credentials = file("D:/Study/key/norse-case-439506-h4-783c3e3d6177.json")  # Đường dẫn tới file JSON
  project     = "norse-case-439506-h4"  # Đúng ID dự án
  region      = "us-central1"
}
