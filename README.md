# URL Shortener (WIP)

Simple URL shortener implemented with scala and Cats. This project is not meant for production use. 
The project is being used for educational content I'm creating.

## Tools and Libraries

- Scala 2.13
- [Cats](https://typelevel.org/cats/) FP library proving several utilities such as common type-classes.
- [Doobie](https://tpolecat.github.io/doobie/) for SQL data layer.
- [HTTP4s](https://http4s.org/) For REST API implementation
- [Scalatest](https://www.scalatest.org/) For general testing.
- [Scalacheck](https://github.com/typelevel/scalacheck) Property testing for Scala.
- [Flyway](https://flywaydb.org/) Database migrations.

## Development

This project uses [SBT](https://www.scala-sbt.org/) for building, dependency management and general tasks.