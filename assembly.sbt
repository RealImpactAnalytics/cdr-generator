import AssemblyKeys._ // put this at the top of the file

assemblySettings

mainClass in assembly := Some("CDRSimulation")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
	  {
			case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
			case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
			case x if x.contains("META-INF") => MergeStrategy.first
			case x if x.contains("Logger.class") => MergeStrategy.first
			case x if x.contains("Log.class") => MergeStrategy.first
			case "application.conf" => MergeStrategy.concat
			case "unwanted.txt"     => MergeStrategy.discard
			case x => old(x)
		}
}
