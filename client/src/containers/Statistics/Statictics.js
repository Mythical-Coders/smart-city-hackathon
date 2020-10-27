import React from "react";
import GraphPercentageFullness from "./content/GraphPercentageFullness";
import Button from "../../components/CustomButtons/Button";
import { RegionDropdown } from "react-country-region-selector";
import DateTimePicker from "react-datetime-picker";
import NavPills from "../../components/NavPills/NavPills";
import { Schedule, Dashboard } from "@material-ui/icons";
import MostImpoundPlaces from "./content/MostImpoundPlaces";
function Statictics() {
  return (
    <NavPills
      active={1}
      color="chengapp"
      tabs={[
        {
          tabButton: "أكثر الاماكن مخالفة ",
          tabIcon: Dashboard,
          tabContent: (
            <>
               <div style={{backgroundColor:"#D3D3D3", borderRadius:"10px", textAlign:"right", padding:"15px"}}>
                <h3>المنطقة</h3>

                <RegionDropdown
                  defaultOptionLabel="اختر المنطقة"
                  id="region"
                  name="region"
                  country="Tunisia"
                  // value={region}
                  // onChange={(e) => handleChange(e, "region")}
                />
                <h3>Time Start</h3>

                <DateTimePicker
                // onChange={(e) => handleChange(e, "release")}
                // value={release}
                />
                <h3>Time End</h3>

                <DateTimePicker
                // onChange={(e) => handleChange(e, "release")}
                // value={release}
                />

                <Button variant="contained" color="chengapp">
                  اختر
                </Button>
                <br />
                <br />
                <br />
                <MostImpoundPlaces />
              </div>
            </>
          ),
        },
        {
          tabButton: "نسبة إمتلاء مركز الحاجز ",

          tabIcon: Schedule,
          tabContent: (
            <>
              <div style={{backgroundColor:"#D3D3D3", borderRadius:"10px", textAlign:"right", padding:"15px"}}>
                <h3>المنطقة</h3>

                <RegionDropdown
                  defaultOptionLabel="اختر المنطقة"
                  id="region"
                  name="region"
                  country="Tunisia"
                  // value={region}
                  // onChange={(e) => handleChange(e, "region")}
                />
                <h3>Time Start</h3>

                <DateTimePicker
                // onChange={(e) => handleChange(e, "release")}
                // value={release}
                />
                <h3>Time End</h3>

                <DateTimePicker
                // onChange={(e) => handleChange(e, "release")}
                // value={release}
                />

                <Button variant="contained" color="chengapp">
                  اختر
                </Button>
                <br />
                <br />
                <br />
                <GraphPercentageFullness />
              </div>
            </>
          ),
        },
      ]}
    />
  );
}

export default Statictics;
