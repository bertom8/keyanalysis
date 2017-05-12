window.org_keyanalysis_View_CompareComplex = function() {
	var connectorId = this.getConnectorId();
	var element = this.getElement();
	
	this.registerRpc({
		sendComplexTypes: function(list, list2) {
			var data = list;
			var data2 = list2;

			var formatCount = d3.format(",.0f");

			var margin = {top: 10, right: 30, bottom: 30, left: 30},
			    width = 550 - margin.left - margin.right,
			    height = 480 - margin.top - margin.bottom;

			var x = d3.scaleLinear()
		    .rangeRound([0, width])
		    .domain([0,2]);

			var bins = d3.histogram()
			    .domain(x.domain())
			    .thresholds(x.ticks(50))
			    (data);
			
			var bins2 = d3.histogram()
		    	.domain(x.domain())
		    	.thresholds(x.ticks(50))
		    	(data2);
			
			var max1 = d3.max(bins, function(d) { return d.length; });
			var max2 = d3.max(bins2, function(d) { return d.length; });

			var y = d3.scaleLinear()
			    .domain([0, Math.max(max1, max2)])
			    .range([height, 0]);

			var svg = d3.select(element).append("svg")
			    .attr("width", width + margin.left + margin.right)
			    .attr("height", height + margin.top + margin.bottom)
			    .style("position", "relative")
			  .append("g")
			    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			var bar = svg.selectAll(".bar")
			    .data(bins)
			  .enter().append("g")
			    .attr("class", "bar")
			    .attr("transform", function(d) { return "translate(" + x(d.x0) + "," + y(d.length) + ")"; })

			bar.append("rect")
			    .attr("x", 1)
			    .attr("width", x(bins[0].x1) - x(bins[0].x0) - 1)
			    .attr("height", function(d) { return height - y(d.length); });

			bar.append("text")
			    .attr("dy", ".75em")
			    .attr("y", 6)
			    .attr("x", (x(bins[0].x1) - x(bins[0].x0)) / 2)
			    .attr("text-anchor", "middle")
			    .text(function(d) { return formatCount(d.length); });

			svg.append("g")
			    .attr("class", "axis axis--x")
			    .attr("transform", "translate(0," + height + ")")
			    .call(d3.axisBottom(x));
			
			var svg2 = d3.select(element).append("svg")
		    	.attr("width", width + margin.left + margin.right)
		    	.attr("height", height + margin.top + margin.bottom)
		    	.style("position", "absolute")
				.style("top", "0")
				.style("left", "3")
		      .append("g")
		      	.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			var bar2 = svg2.selectAll(".bar")
		    	.data(bins2)
		      .enter().append("g")
		      	.attr("class", "bar2")
		      	.attr("transform", function(d) { return "translate(" + x(d.x0) + "," + y(d.length) + ")"; });

			bar2.append("rect")
		    	.attr("x", 1)
		    	.attr("width", x(bins[0].x1) - x(bins[0].x0) - 1)
		    	.attr("height", function(d) { return height - y(d.length); });

			bar2.append("text")
		    	.attr("dy", ".75em")
		    	.attr("y", 6)
		    	.attr("x", (x(bins[0].x1) - x(bins[0].x0)) / 2)
		    	.attr("text-anchor", "middle")
		    	.text(function(d) { return formatCount(d.length); });
		}
	});
}