package service

import play.api.libs.json.{Json, OFormat}

case class PrincipalDTO(
                         nconst: String,
                         primaryName: String,
                         birthYear: Int
                       )

case class CrewDTO(
                    nconst: String,
                    primaryName: String,
                    birthYear: Int,
                    role : String
                  )

case class MovieWithDetailsDTO(
                             tconst: String,
                             titleType: String,
                             primaryTitle: String,
                             originalTitle: String,
                             crew: Seq[CrewDTO],
                             principals: Seq[PrincipalDTO]
                           )

object MovieWithDetailsDTO {
  implicit val movieWithDetailsFormat: OFormat[MovieWithDetailsDTO] = Json.format[MovieWithDetailsDTO]
}

object PrincipalDTO{
  implicit val principalFormat: OFormat[PrincipalDTO] = Json.format[PrincipalDTO]
}

object CrewDTO{
  implicit val crewFormat: OFormat[CrewDTO] = Json.format[CrewDTO]
}