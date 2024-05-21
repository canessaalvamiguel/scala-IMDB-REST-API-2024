package service.dto

import play.api.libs.json.{Json, OFormat}

case class MovieRatingDTO(
                         name: String,
                         rating: Double,
                         numVotes: Int
                       )

object MovieRatingDTO{
  implicit val movieRatingDTOFormat: OFormat[MovieRatingDTO] = Json.format[MovieRatingDTO]
}