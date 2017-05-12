window.org_keyanalysis_View_LineComplex = function() {
	var connectorId = this.getConnectorId();
	var element = this.getElement();
	
	this.registerRpc({
		sendComplexTypes: function(list, avg, std) {
			var listdata = list;
			var data = [];
			
			// line chart based on http://bl.ocks.org/mbostock/3883245
			var margin = {top: 10, right: 30, bottom: 30, left: 30},
			    width = element.parentElement.offsetWidth - margin.left - margin.right,
			    height = 480 - margin.top - margin.bottom;

			var x = d3.scaleLinear()
			    .rangeRound([0, width])
			    .domain([0,2]);

			var y = d3.scaleLinear()
			    .range([height, 0]);

			var xAxis = d3.axisBottom(x);

			var yAxis = d3.axisLeft(y);
			
			var bins = d3.histogram()
		    	.domain(x.domain())
		    	.thresholds(x.ticks(50))
		    	(listdata);
			
			getData();

			var line = d3.line()
				.curve(d3.curveLinear)
			    .x(function(d) {
			        return x(d.q);
			    })
			    .y(function(d) {
			        return y(d.p);
			    });

			var svg = d3.select(element).append("svg")
			    .attr("width", width + margin.left + margin.right)
			    .attr("height", height + margin.top + margin.bottom)
			    .style("position", "absolute")
				.style("top", "0")
				.style("left", "0")
			    .append("g")
			    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			x.domain(d3.extent(data, function(d) {
			    return d.q;
			}));
			y.domain(d3.extent(data, function(d) {
			    return d.p;
			}));

			/*svg.append("g")
			    .attr("class", "x axis")
			    .attr("transform", "translate(0," + height + ")")
			    .call(xAxis);*/

			/*svg.append("g")
			    .attr("class", "y axis")
			    .call(yAxis);*/

			svg.append("path")
			    .datum(data)
			    .attr("class", "line")
			    .attr("d", line);

			function getData() {
				var average = avg // TODO: new parameter with the average
				var datastd = std // TODO: new parameter with the stds
				var ni1 = 1/(datastd*Math.sqrt(2*Math.PI));
				var length = bins.length;
				var bin_distance = 6;
				var num_records = listdata.length; // TODO: list.length;

				/*for (var i = 0; i < length; i++) {

					//This is the second part of the formula 
					var ni2 = Math.exp(-1*((bins[i].length-average)*(bins[i].length-average))/(2* (datastd*datastd)));

					// this is the final calculation for the norm values. I also rounded the value but thats up to you if you want, you can remove this for unrounded values.   
					var p = Math.round(ni1*ni2*bin_distance*num_records);
					//var q = normal();
					//var p = gaussian(bins[i].length, avg, datastd);
					var q = (bins[i].x1 + bins[i].x0) / 2;
					var e = { 
							'q': q,
							'p': p 
					}
					// TODO: push normdata to y of line array
					data.push(e);
				}*/
					

				// loop to populate data array with 
				// probabily - quantile pairs
				for (var i = 0; i < 10000; i++) {
				    q = normal() // calc random draw from normal dist
				    p = gaussian(q) // calc prob of rand draw
				    el = {
				        "q": q,
				        "p": p
				    }
				    data.push(el)
				};
	
				// need to sort for plotting
				//https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort
				data.sort(function(x, y) {
				    return x.q - y.q;
				});	
			}

			// from http://bl.ocks.org/mbostock/4349187
			// Sample from a normal distribution with mean 0, stddev 1.
			function normal() {
			    var x = 0,
			        y = 0,
			        rds, c;
			    do {
			        x = Math.random() * 2 - 1;
			        y = Math.random() * 2 - 1;
			        rds = x * x + y * y;
			    } while (rds == 0 || rds > 1);
			    c = Math.sqrt(-2 * Math.log(rds) / rds); // Box-Muller transform
			    return x * c; // throw away extra sample y * c
			}

			//taken from Jason Davies science library
			// https://github.com/jasondavies/science.js/
			function gaussian(x) {
				var gaussianConstant = 1 / Math.sqrt(2 * Math.PI),
					mean = 0,
			    	sigma = 1;

			    x = (x - mean) / sigma;
			    return gaussianConstant * Math.exp(-.5 * x * x) / sigma;
			};
			
			/*function gaussian(mean, stdev) {
			    var y2;
			    var use_last = false;
			    return function() {
			        var y1;
			        if(use_last) {
			           y1 = y2;
			           use_last = false;
			        }
			        else {
			            var x1, x2, w;
			            do {
			                 x1 = 2.0 * Math.random() - 1.0;
			                 x2 = 2.0 * Math.random() - 1.0;
			                 w  = x1 * x1 + x2 * x2;               
			            } while( w >= 1.0);
			            w = Math.sqrt((-2.0 * Math.log(w))/w);
			            y1 = x1 * w;
			            y2 = x2 * w;
			            use_last = true;
			       }

			       var retval = mean + stdev * y1;
			       if(retval > 0) 
			           return retval;
			       return -retval;
			   }
			}*/

			
		}
	});
}