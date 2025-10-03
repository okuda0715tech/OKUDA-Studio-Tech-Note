<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Fragment内のViewからのイベントをFragmentで受け取るシンプルな方法](#fragment内のviewからのイベントをfragmentで受け取るシンプルな方法)
<!-- TOC END -->


# Fragment内のViewからのイベントをFragmentで受け取るシンプルな方法

```java
public class StartFragment extends Fragment implements OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Button b = (Button) v.findViewById(R.id.StartButton);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.StartButton:

            ...

            break;
        }
    }
}
```
