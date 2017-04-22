resource "aws_s3_bucket" "pipeline" {
  bucket = "run-it-spark"
}

resource "aws_cloudformation_stack" "data_pipeline" {
  name = "DataPipeline"
  template_body = "${data.template_file.cloud_formation_template.rendered}"
}

data "template_file" "cloud_formation_template" {
  template = "${file("pipeline.cf.json")}"
  vars {
    pipeline_name = "Pipeline"

    period = "24 Hour"

    s3_location_for_logs = "${aws_s3_bucket.pipeline.bucket}/pipeline-logs"
    subnet = "subnet-id"

    bootstrap_script_location = "s3://${aws_s3_bucket_object.emr_bootstrap.bucket}/${aws_s3_bucket_object.emr_bootstrap.key}"

    cluster_core_instences_count = "3"
  }
}

data "template_file" "emr_bootstrap" {
  template = "${file("emr_bootstrap.sh")}"
  vars {
    bucket = "${aws_s3_bucket.pipeline.bucket}"
  }
}

resource "aws_s3_bucket_object" "emr_bootstrap" {
  bucket = "${aws_s3_bucket.pipeline.id}"
  key = "emr_bootstrap.sh"
  content = "${data.template_file.emr_bootstrap.rendered}"
}
