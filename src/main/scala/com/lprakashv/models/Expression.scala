package com.lprakashv.models

trait Expression extends Product {
  def eval: Value
}