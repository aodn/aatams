#!/usr/bin/env ruby
require 'httpclient'
require 'nokogiri'
require 'trollop'

#
# Feed a list of filenames to this script, as such:
#
# # This *should* only include detections files (as opposed to events files also).
# $ grep -Rl "Transmitter" * | ~/git/aatams/doc/csv_upload/upload_csv_files.rb
#

$http = HTTPClient.new

def authenticate(aatams_url)
  $http.post "#{aatams_url}/auth/signIn", :username => 'jkburges', :password => 'password'
end

def upload_files(aatams_url)
  ARGF.each_line { |upload_file_path|
    upload_file aatams_url, upload_file_path
  }
end

def upload_file(aatams_url, upload_file_path)
  puts "Uploading file: #{upload_file_path}"

  upload_file = File.new(upload_file_path.chomp)

  response = $http.post "#{aatams_url}/receiverDownloadFile/save?format=xml",
  :type => 'DETECTIONS_CSV',
  File.basename(upload_file_path) => upload_file

  wait_for_processing_to_end response.headers['Location']
end

def wait_for_processing_to_end(show_upload_url)
  status = 'PROCESSING'

  while status == 'PROCESSING' do
    sleep 2
    upload_response = Nokogiri::XML($http.get(show_upload_url).content)
    statusAttr = upload_response.xpath('//receiverDownloadFile/@status')[0]

    status = statusAttr.to_str
  end
end

def main

  opts = Trollop::options do
    opt :url, "AATAMS URL", :default => "http://localhost:8080/aatams"
  end

  authenticate opts[:url]
  upload_files opts[:url]
end

main
